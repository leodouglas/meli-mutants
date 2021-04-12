package com.mercadolibre.mutants.services.impl;

import com.mercadolibre.mutants.configs.commons.ErrorCode;
import com.mercadolibre.mutants.domains.Dna;
import com.mercadolibre.mutants.exceptions.DNAException;
import com.mercadolibre.mutants.exceptions.InvalidNucleotideException;
import com.mercadolibre.mutants.repositories.DnaRepository;
import com.mercadolibre.mutants.services.DnaService;
import com.mercadolibre.mutants.services.commons.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DnaServiceImpl implements DnaService {

    private final DnaRepository repository;
    private final Base64.Encoder base64encoder = Base64.getEncoder();
    private static final Logger LOGGER = LoggerFactory.getLogger(DnaServiceImpl.class);

    private static final Set<Character> validNucleotides = new HashSet<>(Arrays.asList('A', 'T', 'C', 'G'));
    private static final int MUTANT_REPEATED_STRAND = 4;

    public DnaServiceImpl(DnaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isMutant(String[] dna) throws DNAException {
        LOGGER.debug("Start analyzing dna");
        if (dna == null) {
            LOGGER.info("Invalid DNA");
            throw new DNAException("Invalid DNA", null, ErrorCode.INVALID_DATA);
        }
        var strands = Arrays.stream(dna).map(String::toCharArray).toArray(char[][]::new);
        String checksum = generateChecksum(strands);
        LOGGER.info("Looking DNA: {}", checksum);
        Dna stored = this.repository.findByChecksum(checksum);
        if (stored != null) {
            LOGGER.debug("DNA already stored {}", checksum);
            LOGGER.info("DNA {} has mutant strand: {}", checksum, stored.isMutant());
            return stored.isMutant();
        }
        checkNucleotides(strands);
        boolean mutant = hasMutantStrands(strands);
        storeAnalysis(checksum, strands, mutant);
        LOGGER.info("DNA {} has mutant strand: {}", checksum, mutant);
        return mutant;
    }

    @Override
    public Stats getStats() {
        Long mutants = repository.countAllMutants();
        Long humans = repository.countAllHumans();
        float ratio = Float.max(mutants, 1) / Float.max(humans, 1);
        return new Stats(mutants, humans, ratio);
    }

    @Override
    public void flush() {
        repository.deleteAll();
    }

    public String generateChecksum(final char[][] strands) {
        var value = Arrays.stream(strands).map(String::new).collect(Collectors.joining("-"));
        return base64encoder.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private void storeAnalysis(final String checksum, final char[][] strands, boolean mutant) {
        final Dna dna = new Dna();
        dna.setChecksum(checksum);
        dna.setMutant(mutant);
        dna.setStrand(strands);
        repository.save(dna);
    }

    private void checkNucleotides(final char[][] strands) throws InvalidNucleotideException {
        for (char[] strand : strands) {
            for (char nucleotides : strand) {
                if (!validNucleotides.contains(nucleotides)) {
                    LOGGER.debug("Invalid DNA");
                    throw new InvalidNucleotideException("This DNA chain does not correspond to any known being. Possibly alien DNA.");
                }
            }
        }
    }

    private boolean hasMutantStrands(final char[][] strands) {
        var future = CompletableFuture.completedFuture(strands);

        var checkHorizontal = future.thenApplyAsync(this::findHorizontalsStrands);
        var checkVertical = future.thenApplyAsync(this::findVerticalsStrands);
        var checkDiagonal = future.thenApplyAsync(this::findDiagonalsStrands);

        return Stream.of(checkHorizontal, checkVertical, checkDiagonal)
                .map(CompletableFuture::join)
                .reduce(Integer::sum)
                .orElse(0) > 0;
    }

    private int findHorizontalsStrands(final char[][] strands) {
        int rows = strands.length;
        int columns = strands[0].length;
        int strandsFound = 0;
        for (int i = 0; i < rows; i++) {
            Character lastNucleotide = null;
            int found = 0;
            for (int j = 0; j < columns; j++) {
                char nucleotide = strands[i][j];
                if (lastNucleotide != null && lastNucleotide.equals(nucleotide)) {
                    found++;
                    if (found >= MUTANT_REPEATED_STRAND - 1) {
                        LOGGER.debug("Mutant dna found in horizontal strand");
                        strandsFound++;
                    }
                } else {
                    found = 0;
                }
                lastNucleotide = nucleotide;
            }
        }
        return strandsFound;
    }

    private int findVerticalsStrands(final char[][] strands) {
        int rows = strands.length;
        int columns = strands[0].length;
        int strandsFound = 0;
        for (int i = 0; i < columns; i++) {
            Character lastNucleotide = null;
            int found = 0;
            for (int j = 0; j < rows; j++) {
                char nucleotide = strands[j][i];
                if (lastNucleotide != null && lastNucleotide.equals(nucleotide)) {
                    found++;
                    if (found >= MUTANT_REPEATED_STRAND - 1) {
                        LOGGER.debug("Mutant dna found in Vertical strand");
                        strandsFound++;
                    }
                } else {
                    found = 0;
                }
                lastNucleotide = nucleotide;
            }
        }
        return strandsFound;
    }

    private int findDiagonalsStrands(final char[][] strands) {
        int rows = strands.length;
        int columns = strands[0].length;
        int strandsFound = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char nucleotide = strands[i][j];
                if (findDiagonalsStrandsRight(i, j, nucleotide, strands)) {
                    LOGGER.debug("Mutant dna found in diagonal right strand");
                    strandsFound++;
                }
                if (findDiagonalsStrandsLeft(i, j, nucleotide, strands)) {
                    LOGGER.debug("Mutant dna found in diagonal left strand");
                    strandsFound++;
                }
            }
        }
        return strandsFound;
    }

    private boolean findDiagonalsStrandsRight(int row, int column, final char nucleotide, final char[][] strands) {
        int rows = strands.length;
        int columns = strands[0].length;
        int maxLoop = Integer.min(rows - row - 1, columns - column - 1);
        if (maxLoop >= MUTANT_REPEATED_STRAND - 1) {
            for (int i = 1; i <= maxLoop; i++) {
                if (nucleotide != strands[row + i][column + i]) {
                    return false;
                }
                if (i >= MUTANT_REPEATED_STRAND - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean findDiagonalsStrandsLeft(int row, int column, final char nucleotide, final char[][] strands) {
        int rows = strands.length;
        int maxLoop = Integer.min(rows - row - 1, column);
        if (maxLoop >= MUTANT_REPEATED_STRAND - 1) {
            for (int i = 1; i <= maxLoop; i++) {
                if (nucleotide != strands[row + i][column - i]) {
                    return false;
                }
                if (i >= MUTANT_REPEATED_STRAND - 1) {
                    return true;
                }
            }
        }
        return false;
    }
}

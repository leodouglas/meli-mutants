package com.mercadolibre.mutants.services;

import com.mercadolibre.mutants.domains.Dna;
import com.mercadolibre.mutants.exceptions.DNAException;
import com.mercadolibre.mutants.exceptions.InvalidNucleotideException;
import com.mercadolibre.mutants.repositories.DnaRepository;
import com.mercadolibre.mutants.services.commons.Stats;
import com.mercadolibre.mutants.services.impl.DnaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DnaServiceTest {

    private DnaService dnaService;

    @Mock
    private DnaRepository repository;

    @BeforeEach
    void setUp() {
        dnaService = new DnaServiceImpl(repository);
    }

    private static Stream<Arguments> provideDNAData() {
        return Stream.of(
                Arguments.of(new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATGT",
                        "AGAAGG",
                        "CCCCTA",
                        "TCACTG"}, true),
                Arguments.of(new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATTT",
                        "AGACGG",
                        "GCGTCA",
                        "TCACTG"}, false),
                Arguments.of(new String[]{
                        "CCCCA",
                        "CAGTGC",
                        "TTATTT",
                        "AGACGG",
                        "GCGTCA",
                        "TCACTG"}, true),
                Arguments.of(new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATTT",
                        "AGACGG",
                        "GCGTCA",
                        "TCCCCC"}, true),
                Arguments.of(new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATGT",
                        "AGACGG",
                        "GCGTCA",
                        "TCACTG"}, true),
                Arguments.of(new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATTT",
                        "AGACTG",
                        "GCGTTA",
                        "TCACTG"}, true),
                Arguments.of(new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTGTTT",
                        "AGAGGG",
                        "GCGTGA",
                        "TCACTG"}, true),
                Arguments.of(new String[]{
                        "ATGCGA",
                        "CAGTGC",
                        "TTATTT",
                        "AGTCGG",
                        "GTGTCA",
                        "TCACTG"}, true),
                Arguments.of(new String[]{
                        "ATGCGA",
                        "CAGTAC",
                        "TTAATT",
                        "AGACGG",
                        "GCGTCA",
                        "TCACTG"}, true)
        );
    }

    private static Stream<Arguments> provideStatsData() {
        return Stream.of(
                Arguments.of(40L, 100L, 0.4F),
                Arguments.of(0L, 0L, 1F),
                Arguments.of(1L, 1L, 1F),
                Arguments.of(2L, 1L, 2F)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDNAData")
    public void isMutant(String[] dna, boolean result) throws DNAException {
        assertEquals(dnaService.isMutant(dna), result);
    }

    @Test
    public void invalidDna() throws DNAException {
        assertThrows(InvalidNucleotideException.class, () -> {
            String[] dna = {
                    "DTGCGA",
                    "CAGTAC",
                    "TTAATT",
                    "AGACGG",
                    "GCGTCA",
                    "TCACTG"};
            dnaService.isMutant(dna);
        });
    }

    @Test
    public void invalidNullDna() throws DNAException {
        assertThrows(DNAException.class, () -> {
            dnaService.isMutant(null);
        });
    }

    @Test
    public void generateChecksum() throws DNAException {
        Dna dna = new Dna();
        char[][] strand = {{'T', 'G', 'C', 'G'}, {'T', 'G', 'C', 'G'}, {'T', 'G', 'C', 'G'}, {'T', 'G', 'C', 'G'}};
        dna.setStrand(strand);
        dna.setChecksum(dnaService.generateChecksum(strand));
        dna.setId(1L);
        dna.setMutant(true);

        dnaService.isMutant(new String[]{"TGCG", "TGCG", "TGCG", "TGCG"});
        verify(repository, times(1)).findByChecksum(dnaService.generateChecksum(strand));
    }

    @ParameterizedTest
    @MethodSource("provideStatsData")
    public void checkStats(Long mutants, Long humans, Float ratio) {
        when(repository.countAllMutants()).thenReturn(mutants);
        when(repository.countAllHumans()).thenReturn(humans);
        Stats stats = dnaService.getStats();
        assertEquals(stats.getCountMutant(), mutants);
        assertEquals(stats.getCountHuman(), humans);
        assertEquals(stats.getRatio(), ratio);
        assertNotNull(stats.toString());
    }

    @Test
    public void checkDnaDomain() throws DNAException {
        Dna dna = new Dna();
        char[][] strand = {};
        dna.setStrand(strand);
        dna.setChecksum("ASD");
        dna.setId(1L);
        dna.setMutant(true);

        assertEquals(dna.getChecksum(), "ASD");
        assertEquals(dna.getId(), 1);
        assertEquals(dna.getId(), 1);
        assertEquals(dna.getStrand(), strand);
        assertTrue(dna.isMutant());
        assertNotNull(dna.toString());
    }
}

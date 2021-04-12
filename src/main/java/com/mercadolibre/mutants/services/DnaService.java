package com.mercadolibre.mutants.services;

import com.mercadolibre.mutants.exceptions.DNAException;
import com.mercadolibre.mutants.services.commons.Stats;

public interface DnaService {

    boolean isMutant(String[] dna) throws DNAException;

    String generateChecksum(final char[][] strands);

    Stats getStats();

    void flush();
}

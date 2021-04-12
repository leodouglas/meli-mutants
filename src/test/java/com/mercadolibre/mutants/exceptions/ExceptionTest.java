package com.mercadolibre.mutants.exceptions;

import com.mercadolibre.mutants.configs.commons.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    public void checkInvalidMutantDnaException(){
        InvalidMutantDNAException invalidDna = new InvalidMutantDNAException("Invalid Dna");
        assertEquals(invalidDna.getMessage(), "Invalid Dna");
    }

    @Test
    public void checkDnaException(){
        DNAException invalidDna = new InvalidNucleotideException("Invalid Dna");
        assertEquals(invalidDna.getMessage(), "Invalid nucleotide. Invalid Dna");
        assertEquals(invalidDna.getCode(), ErrorCode.INVALID_NUCLEOTIDE);
    }

}

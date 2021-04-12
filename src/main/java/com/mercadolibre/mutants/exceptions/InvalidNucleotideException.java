package com.mercadolibre.mutants.exceptions;

import com.mercadolibre.mutants.configs.commons.ErrorCode;

public class InvalidNucleotideException extends DNAException {

    public InvalidNucleotideException(String s) {
        super("Invalid nucleotide.", s, ErrorCode.INVALID_NUCLEOTIDE);
    }
}

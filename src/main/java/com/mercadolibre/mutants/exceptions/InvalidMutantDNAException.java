package com.mercadolibre.mutants.exceptions;

import com.mercadolibre.mutants.configs.commons.ErrorCode;

public class InvalidMutantDNAException extends BasicException {

    public InvalidMutantDNAException(String error) {
        super(error, null, ErrorCode.MUTANT_NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

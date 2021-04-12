package com.mercadolibre.mutants.exceptions;

import com.mercadolibre.mutants.configs.commons.ErrorCode;

public class DNAException extends BasicException {

    public DNAException(String error, String message, ErrorCode code) {
        super(error, message, code);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

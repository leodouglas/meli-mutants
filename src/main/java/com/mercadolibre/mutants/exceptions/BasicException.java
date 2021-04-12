package com.mercadolibre.mutants.exceptions;

import com.mercadolibre.mutants.configs.commons.ErrorCode;

import javax.validation.constraints.NotNull;

public class BasicException extends Throwable {

    private final String message;
    private final ErrorCode code;

    public BasicException(@NotNull String error, String message, ErrorCode code) {
        this.message = message != null ? String.join(" ", error, message) : error;
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

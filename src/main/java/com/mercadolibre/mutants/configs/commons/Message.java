package com.mercadolibre.mutants.configs.commons;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    private final MessageType type;
    private final String message;
    private ErrorCode code;
    private Map<String, Object> errorData;

    public Message(MessageType type, String message, ErrorCode code, Map<String, Object> errorData) {
        this.type = type;
        this.message = message;
        this.code = code;
        this.errorData = errorData;
    }

    public Message(MessageType type, String message, ErrorCode code) {
        this.type = type;
        this.message = message;
        this.code = code;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getCode() {
        return code;
    }

    public Map<String, Object> getErrorData() {
        return errorData;
    }

    public Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }
}

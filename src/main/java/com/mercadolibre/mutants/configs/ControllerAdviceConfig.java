package com.mercadolibre.mutants.configs;

import com.mercadolibre.mutants.configs.commons.Message;
import com.mercadolibre.mutants.configs.commons.MessageType;
import com.mercadolibre.mutants.exceptions.BasicException;
import com.mercadolibre.mutants.exceptions.InvalidMutantDNAException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Locale;

@ControllerAdvice
public class ControllerAdviceConfig {

    private final MessageSource msgSource;

    public ControllerAdviceConfig(MessageSource msgSource) {
        this.msgSource = msgSource;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Message handleExceptionNotFound(NoHandlerFoundException ex) {
        return new Message(MessageType.ERROR, "Endpoint not found");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Message processValidationError(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult().getFieldError();
        assert error != null;
        return createMessage(error.getDefaultMessage());
    }

    private Message createMessage(String message) {
        Locale locale = LocaleContextHolder.getLocale();
        String msg = msgSource.getMessage(message, null, message, locale);
        return new Message(MessageType.ERROR, msg);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidMutantDNAException.class)
    public Message mutantNotFoundException(InvalidMutantDNAException ex) {
        return new Message(MessageType.ERROR, ex.getMessage(), ex.getCode());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BasicException.class)
    public Message handleException(BasicException ex) {
        return new Message(MessageType.ERROR, ex.getMessage(), ex.getCode());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Message handleExceptionMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new Message(MessageType.ERROR, ex.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Message handleExceptionAccessDenied(AccessDeniedException ex) {
        return new Message(MessageType.ERROR, ex.getCause().toString());
    }
}



package com.mercadolibre.mutants.configs.commons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageTest {

    @Test
    void messageBean() {
        new Message(MessageType.ERROR, "", ErrorCode.INVALID_NUCLEOTIDE);
        new Message(MessageType.WARNING, "");
        Message message = new Message(MessageType.ERROR, "", ErrorCode.INVALID_NUCLEOTIDE, Collections.emptyMap());
        assertEquals(message.getMessage(), "");
        assertEquals(message.getCode(), ErrorCode.INVALID_NUCLEOTIDE);
        assertEquals(message.getType(), MessageType.ERROR);
        assertEquals(message.getErrorData(), Collections.emptyMap());
    }

}

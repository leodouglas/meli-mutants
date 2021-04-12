package com.mercadolibre.mutants.controllers.requests;

import com.mercadolibre.mutants.controllers.responses.StatsResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    public void analyzeRequestTest(){
        var request = new AnalyzeRequest();
        String[] dna = {};
        request.setDna(dna);
        assertEquals(request.getDna(), dna);
        assertNotNull(request.toString());
    }

    @Test
    public void statsRequestTest(){
        var response = new StatsResponse();
        response.setCountHumanDna(1L);
        response.setCountMutantDna(2L);
        response.setRatio(1F);

        assertEquals(response.getCountHumanDna(), 1L);
        assertEquals(response.getCountMutantDna(), 2L);
        assertEquals(response.getRatio(), 1F);
        assertNotNull(response.toString());
    }

}

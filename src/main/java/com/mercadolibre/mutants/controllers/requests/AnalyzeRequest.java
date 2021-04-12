package com.mercadolibre.mutants.controllers.requests;

import java.util.Arrays;
import javax.validation.constraints.NotNull;

public class AnalyzeRequest {

    @NotNull(message = "dna is a required field")
    private String[] dna;

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }

    @Override
    public String toString() {
        return "AnalyzeRequest{" +
                "dna=" + Arrays.toString(dna) +
                '}';
    }
}

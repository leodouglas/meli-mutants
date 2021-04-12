package com.mercadolibre.mutants.controllers;

import com.mercadolibre.mutants.controllers.requests.AnalyzeRequest;
import com.mercadolibre.mutants.exceptions.BasicException;
import com.mercadolibre.mutants.exceptions.InvalidMutantDNAException;
import com.mercadolibre.mutants.services.DnaService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/mutant")
public class AnalyzeController {

    private final DnaService service;

    public AnalyzeController(DnaService service) {
        this.service = service;
    }

    @PostMapping
    public void analyze(@RequestBody @Valid final AnalyzeRequest request) throws BasicException {
        if (!service.isMutant(request.getDna())) {
            throw new InvalidMutantDNAException("No mutant DNA traces were found");
        }
    }


}

package com.mercadolibre.mutants.controllers;

import com.mercadolibre.mutants.controllers.responses.StatsResponse;
import com.mercadolibre.mutants.exceptions.BasicException;
import com.mercadolibre.mutants.services.DnaService;
import com.mercadolibre.mutants.services.commons.Stats;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/stats")
public class AnalyzeStatsController {

    private final DnaService service;

    public AnalyzeStatsController(DnaService service) {
        this.service = service;
    }

    @GetMapping
    public StatsResponse stats() throws BasicException {
        Stats stats = service.getStats();
        StatsResponse response = new StatsResponse();
        response.setCountHumanDna(stats.getCountHuman());
        response.setCountMutantDna(stats.getCountMutant());
        response.setRatio(stats.getRatio());
        return response;
    }

    @DeleteMapping("/flush")
    public StatsResponse flush() throws BasicException {
        service.flush();
        Stats stats = service.getStats();
        StatsResponse response = new StatsResponse();
        response.setCountHumanDna(stats.getCountHuman());
        response.setCountMutantDna(stats.getCountMutant());
        response.setRatio(stats.getRatio());
        return response;
    }

}

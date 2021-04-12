package com.mercadolibre.mutants.services.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

    @Test
    public void checkStatsCommon() {
        Stats stats = new Stats(1L, 2L, 0.5F);
        assertEquals(stats.getRatio(), 0.5F);
        assertEquals(stats.getCountHuman(), 2L);
        assertEquals(stats.getCountMutant(), 1L);
        assertNotNull(stats.toString());
    }

}

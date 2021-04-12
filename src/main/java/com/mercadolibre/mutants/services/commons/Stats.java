package com.mercadolibre.mutants.services.commons;

public class Stats {

    private final Long countMutant;
    private final Long countHuman;
    private final float ratio;

    public Stats(Long countMutant, Long countHuman, float ratio) {
        this.countMutant = countMutant;
        this.countHuman = countHuman;
        this.ratio = ratio;
    }

    public Long getCountMutant() {
        return countMutant;
    }

    public Long getCountHuman() {
        return countHuman;
    }

    public float getRatio() {
        return ratio;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "countMutant=" + countMutant +
                ", countHuman=" + countHuman +
                ", ratio=" + ratio +
                '}';
    }
}

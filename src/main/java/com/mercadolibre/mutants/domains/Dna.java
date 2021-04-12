package com.mercadolibre.mutants.domains;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Entity
@Table(name = "dnas",
        uniqueConstraints = @UniqueConstraint(columnNames = "checksum", name = "dna_checksum"),
        indexes = @Index(columnList = "checksum", name = "dna_checksum_index"))
public class Dna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @NotNull
    @Basic(optional = false)
    private char[][] strand;

    @NotBlank
    @Basic(optional = false)
    private String checksum;

    @NotNull
    @Basic(optional = false)
    private boolean mutant = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public char[][] getStrand() {
        return strand;
    }

    public void setStrand(char[][] strand) {
        this.strand = strand;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        return "Dna{" +
                "id=" + id +
                ", strand=" + Arrays.toString(strand) +
                ", checksum='" + checksum + '\'' +
                '}';
    }

    public boolean isMutant() {
        return mutant;
    }

    public void setMutant(boolean mutant) {
        this.mutant = mutant;
    }
}

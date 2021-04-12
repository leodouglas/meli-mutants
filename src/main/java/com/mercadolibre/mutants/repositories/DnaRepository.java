package com.mercadolibre.mutants.repositories;

import com.mercadolibre.mutants.domains.Dna;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DnaRepository extends CrudRepository<Dna, Long> {

    @Query("select d from Dna d where d.checksum = :checksum")
    Dna findByChecksum(@Param("checksum") String checksum);

    @Query("select count(d) from Dna d where d.mutant = false")
    Long countAllHumans();

    @Query("select count(d) from Dna d where d.mutant = true")
    Long countAllMutants();
}

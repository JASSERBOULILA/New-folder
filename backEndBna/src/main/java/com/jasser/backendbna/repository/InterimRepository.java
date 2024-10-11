package com.jasser.backendbna.repository;


import com.jasser.backendbna.models.Interim;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterimRepository extends CrudRepository<Interim, Long> {

    List<Interim> findAll();

    Optional<Interim> findByMatricule(String matricule);
}

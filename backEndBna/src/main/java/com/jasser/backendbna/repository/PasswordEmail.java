package com.jasser.backendbna.repository;


import com.jasser.backendbna.models.PasswordEmailData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordEmail extends CrudRepository<PasswordEmailData , Long> {
    List<PasswordEmailData> findAll();
}

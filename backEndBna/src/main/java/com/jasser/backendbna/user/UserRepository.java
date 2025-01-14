package com.jasser.backendbna.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByMatricule(String matricule);

  List<User> findAll();

}

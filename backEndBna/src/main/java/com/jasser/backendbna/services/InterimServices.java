package com.jasser.backendbna.services;


import com.jasser.backendbna.models.Interim;
import com.jasser.backendbna.repository.InterimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterimServices {

    private final InterimRepository interimRepository;


    public List<Interim> findAll(){
        return interimRepository.findAll();
    }


    public Interim createNewOne(Interim interim){
        return interimRepository.save(interim);
    }

    public Interim findById(Long id){
        Optional<Interim> oneInterim = interimRepository.findById(id);
        return oneInterim.orElse(null);
    }

    public Interim findByMatricule(String matricule) {
        Optional<Interim> oneInterim = interimRepository.findByMatricule(matricule);
        return oneInterim.orElse(null);
    }


}

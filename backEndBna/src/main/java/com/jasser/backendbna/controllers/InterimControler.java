package com.jasser.backendbna.controllers;


import com.jasser.backendbna.models.Interim;
import com.jasser.backendbna.services.InterimServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interim")
public class InterimControler {

    private final InterimServices  interimServices;



    @GetMapping("/allInterim")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> allInterim() {
        try {
            List<Interim> interims = interimServices.findAll();
            System.out.println("the interim data : "  + interims);
            if (interims.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No interim records found.");
            }
            return ResponseEntity.ok().body(interims);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching interim records.");
        }
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> newInterim(@RequestBody Interim interim){
        try {
            Interim interim1 = interimServices.createNewOne(interim);
            return ResponseEntity.ok().body(interim1);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error Creating Interim: " + e.getMessage());
        }
    }

    @GetMapping("/getInterim/{userMatricule}")
    public ResponseEntity<?> getInterimByUser(@PathVariable String userMatricule) {
        try {
            if (userMatricule == null || userMatricule.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Matricule cannot be null or empty");
            }

            Interim getOneInterim = interimServices.findByMatricule(userMatricule);

            if (getOneInterim == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interim with matricule " + userMatricule + " not found");
            }

            return ResponseEntity.ok().body(getOneInterim);
        } catch (ResponseStatusException ex) {
            // If you want to log or process the exception in a specific way
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception ex) {
            // Catch any other unforeseen errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());

        }

    }
}

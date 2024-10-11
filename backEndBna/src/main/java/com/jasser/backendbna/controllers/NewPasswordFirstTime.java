package com.jasser.backendbna.controllers;

import com.jasser.backendbna.methods.PasswordGenerator;
import com.jasser.backendbna.models.PasswordEmailData;
import com.jasser.backendbna.services.EmailService;
import com.jasser.backendbna.services.PasswordEmailServices;
import com.jasser.backendbna.user.User;
import com.jasser.backendbna.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/passwordFirst")
public class NewPasswordFirstTime {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEmailServices passwordEmailServices;
    @GetMapping("/getUser/{matricule}")
    public ResponseEntity<?> getUserByMatricule(@PathVariable String matricule){
        System.out.println("the user matricule : " + matricule);
        User oneUser = userService.getUser(matricule);
        if(!oneUser.equals(null) ){
            return ResponseEntity.ok().body(oneUser);
        }else{
            return ResponseEntity.badRequest().body("There is no user match the matricule number ");
        }
    }

    @GetMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestParam String recipientEmail){
        System.out.println("The Email Is : " + recipientEmail);
        String genratedPassword = PasswordGenerator.generatePassword();
        boolean emailSendResult = emailService.sendEmail(recipientEmail , genratedPassword);
        if (emailSendResult){
            passwordEmailServices.createPasswordGenerateStringd(genratedPassword);
            return ResponseEntity.status(200).body("The Email Been Sent And The Password Been Stored ");

        }else{
            return ResponseEntity.badRequest().body("cant send Email Sry Error !");
        }
    }

    @GetMapping("/checkThePassword")
    public ResponseEntity<?> checkPassword(@RequestParam String passwords, @RequestParam String matricule) {
        List<PasswordEmailData> allPasswords = passwordEmailServices.allPasswords();

        // Check if the password exists in the list
        Optional<PasswordEmailData> matchingPassword = allPasswords.stream()
                .filter(p -> p.getPasswords().equals(passwords))
                .findFirst();

        if (matchingPassword.isPresent()) {
            // Update user password
            var result = userService.updateUserPassword(matricule, passwords);

            if (result != null) {
                // Prepare success response
                Map<String, String> response = new HashMap<>();
                response.put("message", "The User Password Has Been Updated");
                response.put("password", passwords);

                return ResponseEntity.ok().body(response);
            } else {
                return ResponseEntity.status(404).body(Collections.singletonMap("error", "User not found"));
            }
        }

        // If password not found, return 404
        return ResponseEntity.status(404).body(Collections.singletonMap("error", "Password not found"));
    }





}

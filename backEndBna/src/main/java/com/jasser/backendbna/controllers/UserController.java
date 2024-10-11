package com.jasser.backendbna.controllers;

import com.jasser.backendbna.services.TokenService;
import com.jasser.backendbna.services.UserService;
import com.jasser.backendbna.user.ChangePasswordRequest;
import com.jasser.backendbna.user.User;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService service;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;
    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allUsers")
    public List<User> allUser() throws InstantiationException, IllegalAccessException {
        return service.getAllUsers();
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            tokenService.delteTokenForUser(id);
            service.deleteUser(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        }catch (Exception e) {
            // Generic error handling
            return new ResponseEntity<>("An error occurred while deleting the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        System.out.println("the endpoint work :");
        User oneUser = service.getUser(id);
        if (oneUser != null) {
            // Decode the password (assuming it's Base64 encoded)

            // Create a HashMap to store matricule and decoded password
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("matricule", oneUser.getMatricule());
            responseMap.put("password", oneUser.getPassword());

            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/editUserPassword/{matricule}")
    public ResponseEntity<?> editUserPassword(@RequestBody String password,
                                              @PathVariable String matricule)
    {
        try {
            // Fetch the user by matricule
            User user = service.getUser(matricule);
            if (user == null) {
                // If user with matricule doesn't exist, return not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Encode the new password using PasswordEncoder
            String encodedPassword = passwordEncoder.encode(password);

            // Set the encoded password on the user
            user.setPassword(encodedPassword);

            // Update the user with the new password
            service.updateUser(user);

            // Return success response
            return ResponseEntity.ok("Password updated successfully");

        } catch (Exception e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating password: " + e.getMessage());
        }
    }


}

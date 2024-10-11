package com.jasser.backendbna.admin;


import com.jasser.backendbna.services.UserService;
import com.jasser.backendbna.user.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String get() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Logged-in User Details: " + userDetails.getUsername());
        System.out.println("Authorities: " + userDetails.getAuthorities());

        return "GET:: admin controller";
    }

    @Hidden
    @PreAuthorize("hasAnyAuthority('admin:read')")
    @GetMapping("/getUser")
    public User getUserData(@RequestParam String matricule){
            User oneUser = userService.getUser(matricule);
            if(oneUser != null){
                return oneUser;
            }else{
                return null;
            }
    }
    @PostMapping
    public String post() {
        return "POST:: admin controller";
    }

    @DeleteMapping
    public String delete() {
        return "DELETE:: admin controller";
    }

    @PreAuthorize("hasAnyAuthority('admin:read')")
    @GetMapping("/allUsers")
    public List<User> allUser() {
        return userRepository.findAll();
    }


    @Hidden
    @PreAuthorize("hasAnyAuthority('admin:update')")
    @PutMapping("/editUser/{matricule}")
    public ResponseEntity<Map<String, String>> editUser(@PathVariable String matricule, @RequestBody UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        User editedUser = userService.editUser(userDTO, matricule);
        Map<String, String> response = new HashMap<>();

        if (editedUser != null) {
            response.put("message", "User with matricule " + matricule + " has been updated");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User with matricule " + matricule + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Hidden
    @PreAuthorize("hasAnyAuthority('admin:update')")
    @PatchMapping("/editUser/{matricule}")
    public ResponseEntity<Map<String , String>> editUserRole(@PathVariable String matricule , @RequestBody UserRoleDto userDTO){
        User editRole = userService.editRole(userDTO , matricule);
        Map<String , String> response = new HashMap<>();
        if (editRole != null) {
            response.put("message", "User with matricule " + matricule + " has been updated");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User with matricule " + matricule + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}

package com.jasser.backendbna.auth;

import com.jasser.backendbna.config.LogoutService;
import com.jasser.backendbna.user.User;
import com.jasser.backendbna.user.UserDTO;
import com.jasser.backendbna.user.UserRepository;
import com.jasser.backendbna.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {
  @Autowired
  private final UserService userService;
  private final AuthenticationService service;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private LogoutService logoutService;
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @GetMapping("/getUser")
  public ResponseEntity<User> getUser(@RequestParam String token) {
    Integer userId = service.getUserIdFromToken(token);
    Optional<User> userOptional = userRepository.findById(userId);
    return userOptional
            .map(user -> ResponseEntity.ok().body(user))
            .orElse(ResponseEntity.notFound().build());
  }


  @PutMapping("/editUser")
  public ResponseEntity<?> editUser(@RequestBody UserDTO userDTO){
    System.out.println("The User Data Is : " + userDTO);
    User editedUser = userService.editUser(userDTO , userDTO.getMatricule());
    return ResponseEntity.ok("The User Has Been Updated " + editedUser);
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
    logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    System.out.println("The Logout Succesfully ");
    return ResponseEntity.ok("Logged out successfully");
  }


}

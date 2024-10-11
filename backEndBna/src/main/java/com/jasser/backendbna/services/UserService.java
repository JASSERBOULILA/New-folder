package com.jasser.backendbna.services;

import com.jasser.backendbna.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public User editUser(UserDTO userDto, String matricule) {
        Optional<User> optionalUser = repository.findByMatricule(matricule);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setCin(userDto.getCin());
            user.setEmail(userDto.getEmail());
            user.setLastname(userDto.getLastname());
            user.setFirstname(userDto.getFirstname());
            user.setCodeDivision(userDto.getCodeDivision());
            user.setStructure(userDto.getStructure());
            user.setStatus(userDto.getStatus());
            // Save the updated user
            user.setId(optionalUser.get().getId());
            return repository.save(user);
        } else {
            throw new IllegalStateException("User not found with matricule: " + matricule);
        }
    }

    public User editRole(UserRoleDto userDTO , String matricule){
        Optional<User> optionalUser = repository.findByMatricule(matricule);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setRole(userDTO.getRole());
            user.setStructure(userDTO.getStructure());
            user.setId(optionalUser.get().getId());
            return repository.save(user);
        }else{
            throw new IllegalStateException("User Not Found With Matricule " + matricule);
        }
    }
    public User getUser(String matricule){
        Optional<User> oneUser = repository.findByMatricule(matricule);
        return oneUser.orElse(null);
    }

    public User updateUserPassword(String matricule, String password) {
        Optional<User> oneUser = repository.findByMatricule(matricule);
        if (oneUser.isPresent()) {
            User user = oneUser.get();

            // Bcrypt the password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);

            // Set the bcrypt hashed password
            user.setPassword(hashedPassword);
            user.setFirstTime(false);
            // Save the updated user object to persist changes
            repository.save(user);
            return user;
        } else {
            return null;
        }
    }



    public boolean firstTimePassword(String password){
        if (passwordVerification(password)){
            return true;
        }else{
            return false;
        }
    }

    private boolean passwordVerification(String password) {
        boolean hasNumber = false;
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSymbol = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (isSymbol(ch)) {
                hasSymbol = true;
            }

            // If all conditions are met, no need to continue checking
            if (hasNumber && hasUppercase && hasLowercase && hasSymbol) {
                return true;
            }
        }

        return false;
    }
//    this for the symbole check
    private boolean isSymbol(char ch) {
        // Define symbols you consider valid, e.g. !, @, #, etc.
        String symbols = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/\\`~";
        return symbols.indexOf(ch) != -1;
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    // New method to update the user
    public User updateUser(User user) {
        return repository.save(user); // Save and update the user
    }

    public void deleteUser(Integer id){
        repository.deleteById(id);
    }
}

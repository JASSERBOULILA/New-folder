package com.jasser.backendbna.auth;


import com.jasser.backendbna.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String matricule;
  private Role role;
  private Long cin;
  private String structure;
  private String status;
  private Long codeDivision;
  private boolean firstTime;
}

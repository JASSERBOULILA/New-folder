package com.jasser.backendbna.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String matricule;
    private String firstname;
    private String lastname;
    private String email;
    private String status;
    private String  structure;
    private Long cin;
    private Long codeDivision;


}

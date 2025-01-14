package com.jasser.backendbna.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "_passwordEmailData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordEmailData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passwords;

}

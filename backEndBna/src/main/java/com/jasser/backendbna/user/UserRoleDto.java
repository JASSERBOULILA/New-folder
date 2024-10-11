package com.jasser.backendbna.user;


import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleDto {
    private Role role;
    private String structure;
}

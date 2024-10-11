package com.jasser.backendbna.services;


import com.jasser.backendbna.models.PasswordEmailData;
import com.jasser.backendbna.repository.PasswordEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordEmailServices {

    private final PasswordEmail passwordEmail;

    public List<PasswordEmailData> allPasswords(){
        return passwordEmail.findAll();
    }

    public PasswordEmailData createPasswordGenerateStringd(String password){
        var passwordd  = PasswordEmailData.builder()
                .passwords(password)
                .build();
        return passwordEmail.save(passwordd);
    }


}

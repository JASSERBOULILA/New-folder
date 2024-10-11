package com.jasser.backendbna.services;


import com.jasser.backendbna.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;


    public void delteTokenForUser(Integer id){
        tokenRepository.deleteById(id);
    }

}

package com.jasser.backendbna;

import com.jasser.backendbna.auth.AuthenticationService;
import com.jasser.backendbna.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.jasser.backendbna.user.Role.ADMIN;
import static com.jasser.backendbna.user.Role.USER;

@SpringBootApplication
public class BackEndBnaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackEndBnaApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service
    ) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstname("Admin")
                    .lastname("Admin")
                    .email("admin@mail.com")
                    .matricule("4266")
                    .structure("120")
                    .codeDivision(1200L)
                    .status("actif")
                    .cin(1212212L)
                    .firstTime(true)
                    .role(ADMIN)
                    .build();
            System.out.println("Admin token: " + service.register(admin).getAccessToken());

            var user = RegisterRequest.builder()
                    .firstname("User")
                    .lastname("User")
                    .email("User@mail.com")
                    .matricule("4245")
                    .structure("120")
                    .codeDivision(1200L)
                    .status("actif")
                    .firstTime(true)
                    .cin(1212212L)
                    .role(USER)
                    .build();
            System.out.println("User token: " + service.register(user).getAccessToken());

        };
    }
}

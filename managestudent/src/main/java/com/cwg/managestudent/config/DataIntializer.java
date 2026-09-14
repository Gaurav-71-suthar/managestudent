package com.cwg.managestudent.config;

import com.cwg.managestudent.model.Users;
import com.cwg.managestudent.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataIntializer {

    @Bean
    CommandLineRunner loadSampleData(UsersRepository usersRepository,
                                     PasswordEncoder passwordEncoder){
        return (args) -> {

            if(!usersRepository.existsByUsername("admin")){
                Users users = new Users();
                users.setUsername("admin");
                users.setPassword(passwordEncoder.encode("admin@123"));
                users.setActive(true);
                usersRepository.save(users);

            }

        };

    }


}

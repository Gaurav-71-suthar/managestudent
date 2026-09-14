package com.cwg.managestudent.service.impl;
import org.springframework.security.core.userdetails.User;
import com.cwg.managestudent.model.Users;
import com.cwg.managestudent.repository.UsersRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository=usersRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
        Users users=usersRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Invalid username"));
        {
            return User.withUsername(users.getUsername())
                    .password(users.getPassword())
                            .disabled(!users.isActive())
                            .build();
        }
    }

}

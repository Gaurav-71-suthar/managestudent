package com.cwg.managestudent.repository;

import com.cwg.managestudent.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {

    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);

}


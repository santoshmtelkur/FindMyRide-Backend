package com.example.demo.repository;

import com.example.demo.entity.users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<users, Long> {
    users findByEmail(String email);

}

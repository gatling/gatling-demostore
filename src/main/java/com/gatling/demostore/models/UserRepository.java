package com.gatling.demostore.models;

import com.gatling.demostore.models.data.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    
}

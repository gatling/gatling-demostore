package com.gatling.demostore.models;

import com.gatling.demostore.models.data.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByUsername(String username);
    
}

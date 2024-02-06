package com.project.csye6225.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.csye6225.project.pojo.User;

public interface UserRepository extends JpaRepository<User, String> {
    
    User findByUsername(String username);
}
package com.project.csye6225.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.csye6225.project.pojo.VerifyUser;


public interface VerifyUserRepository extends JpaRepository<VerifyUser, String> {

    VerifyUser findByUsername(String username);
    
}

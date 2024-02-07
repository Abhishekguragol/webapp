package com.project.csye6225.project.pojo;

import java.sql.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Component
@Entity
public class User {
    
    // Id of each user
    @Id
    @UuidGenerator
    private String ID;

    // First and last name of user
    private String first_name;
    private String last_name;

    // Username (email)
    @Column(unique = true)
    private String username;

    //  Password
    private String password;

    @Column(nullable = true)
    @CreationTimestamp
    private Date accountCreated;

    @UpdateTimestamp
    @Column(nullable = true)
    private Date accountUpdated;

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(Date accountCreated) {
        this.accountCreated = accountCreated;
    }

    public Date getAccountUpdated() {
        return accountUpdated;
    }

    public void setAccountUpdated(Date accountUpdated) {
        this.accountUpdated = accountUpdated;
    }

    

    


}
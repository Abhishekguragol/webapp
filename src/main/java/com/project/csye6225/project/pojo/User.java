package com.project.csye6225.project.pojo;

import java.sql.Date;
import java.util.UUID;

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
    private String Fname;
    private String Lname;

    // Username (email)
    @Column(unique = true)
    private String username;

    //  Password
    private String password;

    @Column(nullable = true)
    private Date accountCreated;

    @Column(nullable = true)
    private Date accountUpdated;

    

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public Date getAccountCreated() {
        return accountCreated;
    }

    public Date getAccountUpdated() {
        return accountUpdated;
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

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }


}
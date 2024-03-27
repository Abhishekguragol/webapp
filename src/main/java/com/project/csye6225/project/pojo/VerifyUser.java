package com.project.csye6225.project.pojo;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "verify_user")
@Table(name="verify_user")
@Component
public class VerifyUser {

    // User fields
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "verified")
    private boolean verified;

    @Column(name = "email_sent")
    @CreationTimestamp
    private Instant emailSent;

    // Getters and setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }


    public Instant getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Instant emailSent) {
        this.emailSent = emailSent;
    }


}

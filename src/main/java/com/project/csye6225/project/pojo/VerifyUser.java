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
    @Id
    @UuidGenerator
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "verified")
    private boolean verified;

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Column(name = "email_sent")
    @CreationTimestamp
    private Instant emailSent;

    public Instant getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Instant emailSent) {
        this.emailSent = emailSent;
    }


}

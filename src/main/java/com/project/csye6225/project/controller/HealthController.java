package com.project.csye6225.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import com.project.csye6225.project.pojo.User;
import com.project.csye6225.project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
@Controller
@RequestMapping("/healthz")
public class HealthController {

    @Autowired UserService userService;

    @GetMapping()
    public ResponseEntity<Object> healthCheck(HttpServletRequest request){

        // Return HTTP 400 Bad Request if the request has a payload
        if (request.getContentLengthLong() > 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Check database connectivity

            List<User> user = userService.getUsers();

            // Return HTTP 200 OK 
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .build();
        } catch (Exception e) {
            // Return HTTP 503 Service Unavailable
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .cacheControl(CacheControl.noCache())
                    .build();
        }

        
    }

    
    
    
}

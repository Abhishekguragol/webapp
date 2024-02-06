package com.project.csye6225.project.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.csye6225.project.pojo.User;
import com.project.csye6225.project.service.UserService;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@Controller
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserService userService;

    // Get User Information
    @GetMapping("/user/self")
    public  ResponseEntity<Object> fetchUserInfo(@RequestParam(required = false) String params, @RequestBody(required = false) String body, @RequestHeader(required = false) HttpHeaders header) {
        if ((params != null) || body != null) {
            return ResponseEntity.badRequest().build();
        }

        String authHeader = header.getFirst("authorization") != null?header.getFirst("authorization").split(":")[1] : "";
        byte[] decodedAuth =  Base64.getDecoder().decode(authHeader);
        String usernamePass = new String(decodedAuth, StandardCharsets.UTF_8);
        String[] userCreds = usernamePass.split(";");
        User newUser = new User();
        newUser.setUsername(userCreds[0]);
        newUser.setPassword(userCreds[1]);

        if(userService.userLoginAuth(newUser)){
            return  ResponseEntity
            .status(HttpStatusCode.valueOf(204))
            .cacheControl(CacheControl.noCache())
            .body(null);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update user information
    @PutMapping("user/self")
    public ResponseEntity<Object> updateUser(@RequestParam(required = false) String params, @RequestBody(required = false) String body, @RequestHeader(required = false) HttpHeaders header) {
        
        if ((params != null) || body == null ) {
            return ResponseEntity.badRequest().build();
        }
        String authHeader = header.getFirst("authorization") != null?header.getFirst("authorization").split(":")[1] : "";
        byte[] decodedAuth =  Base64.getDecoder().decode(authHeader);
        String usernamePass = new String(decodedAuth, StandardCharsets.UTF_8);
        String[] userCreds = usernamePass.split(";");
        User newUser = new User();
        newUser.setUsername(userCreds[0]);
        newUser.setPassword(userCreds[1]);

        if(userService.userLoginAuth(newUser)){
            return  ResponseEntity
            .status(HttpStatusCode.valueOf(204))
            .cacheControl(CacheControl.noCache())
            .build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
        
    }
     
    
}
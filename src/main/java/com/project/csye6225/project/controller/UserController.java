package com.project.csye6225.project.controller;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Base64;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.csye6225.project.pojo.User;
import com.project.csye6225.project.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@Controller
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserService userService;

    // Get User Information
    @GetMapping("/user/self")
    public  ResponseEntity<Object> fetchUserInfo(@RequestParam Map<String,String> params, @RequestBody(required = false) String body, @RequestHeader(required = false) HttpHeaders header) {
        if ((params.size() > 0) || body != null) {
            return ResponseEntity.badRequest().build();
        }

        if(header.getFirst("authorization") != null) {

            String authHeader = header.getFirst("authorization").split(" ")[1];
            byte[] decodedAuth =  Base64.getDecoder().decode(authHeader);
            String usernamePass = new String(decodedAuth, StandardCharsets.UTF_8);
            String[] userCreds = usernamePass.split(":");
            User newUser = new User();
            newUser.setUsername(userCreds[0]);
            newUser.setPassword(userCreds[1]);

            if(userService.userLoginAuth(newUser)){

                User user = userService.getByName(newUser.getUsername());

                JSONObject obj = jsonMapper(user);
                String str = obj.toString();
            
                return  ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .cacheControl(CacheControl.noCache())
                .body(obj.toString());
            
            }
            else {
                return ResponseEntity.badRequest().build();
            }

        }
        else {
            return ResponseEntity.badRequest().build();
        }
        
    }


    // Update user information
    @PutMapping("user/self")
    public ResponseEntity<Object> updateUser(@RequestParam Map<String,String> params, @RequestBody(required = false) String body, @RequestHeader(required = false) HttpHeaders header) {
        
        if ((params.size() > 0) || body == null ) {
            return ResponseEntity.badRequest().build();
        }
        if(header.getFirst("authorization") != null) {

            String authHeader = header.getFirst("authorization").split(" ")[1];
            byte[] decodedAuth =  Base64.getDecoder().decode(authHeader);
            String usernamePass = new String(decodedAuth, StandardCharsets.UTF_8);
            String[] userCreds = usernamePass.split(":");
            User newUser = new User();
            newUser.setUsername(userCreds[0]);
            newUser.setPassword(userCreds[1]);
            if(userService.userLoginAuth(newUser)){

            ObjectMapper mapper = getMapper();
            User recUser;
            try {
                recUser = mapper.readValue(body, User.class);
                if(recUser.getUsername().equals( userCreds[0])){
                    
                    userService.updateByID(userService
                                            .getByName(recUser.getUsername())
                                            .getID(), 
                                            recUser);
                    return  ResponseEntity
                    .status(HttpStatusCode.valueOf(204))
                    .cacheControl(CacheControl.noCache())
                    .build();

                }
                else {
                    return ResponseEntity.badRequest().build();
                }
                
            } 
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }

            }
            else{
                return ResponseEntity.badRequest().build();
            }
        } 
        else {
            return ResponseEntity.badRequest().build();
        }
    
       
        
    }
     
    // Create a user
    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestParam Map<String,String> params, @RequestBody(required = false) String body, @RequestHeader(required = false) HttpHeaders header) {
        
        if ((params.size() > 0) || body == null) {
            return ResponseEntity.badRequest().build();
        }

        if(header.getFirst("authorization") != null){
            return ResponseEntity.badRequest().build();
        }
        ObjectMapper mapper = getMapper();
        User newUser;
        try {
            newUser = mapper.readValue(body, User.class);
            userService.addUser(newUser);
            String js = mapper.writeValueAsString(newUser);
            return  ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(js);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        
    }

    public ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DateFormat format = DateFormat.getDateTimeInstance();
        mapper.setDateFormat(format);
        return mapper;
    }

    public JSONObject jsonMapper(User user){

        JSONObject obj = new JSONObject();

        obj.put("id", user.getID());
        obj.put("first_name", user.getFirst_name());
        obj.put("last_name", user.getLast_name());
        obj.put("username", user.getUsername());
        obj.put("account_created", user.getAccountCreated());
        obj.put("accpunt_updated", user.getAccountUpdated());

        return obj;
    }

    
}
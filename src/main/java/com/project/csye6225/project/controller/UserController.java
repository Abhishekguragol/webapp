package com.project.csye6225.project.controller;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.csye6225.project.pojo.User;
import com.project.csye6225.project.pojo.VerifyUser;
import com.project.csye6225.project.service.PubSubService;
import com.project.csye6225.project.service.UserService;
import com.project.csye6225.project.service.VerifyUserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@Controller
@RequestMapping("/v1")
public class UserController {

    // Injecting User service  layer to this controller
    @Autowired
    private UserService userService;

    @Autowired
    private VerifyUserService  verifyUserService;

    // Injecting the service needed for  Pub/Sub functionality
    @Autowired
    private PubSubService pubSubService;

    // Logger for errors that occur in application
    Logger logger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_ERROR");

    // Logger for logging  information about requests made by users
    Logger infoLogger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_INFO");


    // API to Get User Information
    @GetMapping("/user/self")
    public  ResponseEntity<Object> fetchUserInfo(@RequestParam Map<String,String> params, @RequestBody(required = false) String body, @RequestHeader(required = false) HttpHeaders header) {
       
        // Returns Bad request in case the request has a body or parameters
        if ((params.size() > 0) || body != null) {
            logger.error("Bad Request: Request contains body");
            return ResponseEntity.badRequest().build();
        }

        // Check if the authorisation header has been sent 
        if(header.getFirst("authorization") != null) {

            //  Fetch the username and password that is sent in the authorisation header
            String authHeader = header.getFirst("authorization").split(" ")[1];
            byte[] decodedAuth =  Base64.getDecoder().decode(authHeader);
            String usernamePass = new String(decodedAuth, StandardCharsets.UTF_8);
            String[] userCreds = usernamePass.split(":");
            User newUser = new User();
            newUser.setUsername(userCreds[0]);
            newUser.setPassword(userCreds[1]);

            // Check if the user credentials are correct
            if(userService.userLoginAuth(newUser)){

                // Fetch the user details from the DB  using the username
                User user = userService.getByName(newUser.getUsername());

                // Map the user name to a Json object to be returned
                JSONObject obj = jsonMapper(user);
            

                infoLogger.info("Succefully fetched user details for:"+userCreds[0]);
                // Return  the response entity with status code OK and the Json Object
                return  ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .cacheControl(CacheControl.noCache())
                .body(obj.toString());
            
            }
            // If  the authentication fails send an Unauthorised message
            else {
                logger.error("Unauthorised access");
                return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
            }

        }
        // If authorizaztion header is not present send  a bad request
        else {
            logger.error("Bad Request: No Auth Header");
            return ResponseEntity.badRequest().build();
        }
        
    }


    // API to Update user information
    @PutMapping("user/self")
    public ResponseEntity<Object> updateUser(@RequestParam Map<String,String> params, @RequestBody(required = false) String body, @RequestHeader(required = false) HttpHeaders header) {
        
        // If parameters are present or if there is not body send  Bad Request
        if ((params.size() > 0) || body == null) {
            logger.error("Bad Request: Request contains body");
            return ResponseEntity.badRequest().build();
        }

        // Check if authorization header is  present, then authenticate the user
        if(header.getFirst("authorization") != null) {

            //  Fetch the username and password that is sent in the authorisation header
            String authHeader = header.getFirst("authorization").split(" ")[1];
            byte[] decodedAuth =  Base64.getDecoder().decode(authHeader);
            String usernamePass = new String(decodedAuth, StandardCharsets.UTF_8);
            String[] userCreds = usernamePass.split(":");
            User newUser = new User();
            newUser.setUsername(userCreds[0]);
            newUser.setPassword(userCreds[1]);
            // Authenticate the user from the database
            if(userService.userLoginAuth(newUser)){

            ObjectMapper mapper = getMapper();
            User recUser;
            try {
                // Map the  received json object into java object
                recUser = mapper.readValue(body, User.class);
                // Check if user is trying to modify account created/updated fields
                if(recUser.getAccountCreated() != null || recUser.getAccountUpdated()!=null || recUser.getUsername() != null) {
                    return ResponseEntity.badRequest().build();
                }
                // Check if the user details being modified are the same as the authenticated user  
                    if(recUser.getPassword() == null) { recUser.setPassword(newUser.getPassword());}
                    // Update the user data with the new data provided in the request
                    userService.updateByID(userService
                                            .getByName(newUser.getUsername())
                                            .getID(), 
                                            recUser);
                    infoLogger.info("Credentials successfully updated for user:"+newUser.getUsername());
                    // If successfully updated return 204 with no conent
                    return  ResponseEntity
                    .status(HttpStatusCode.valueOf(204))
                    .cacheControl(CacheControl.noCache())
                    .build();

                
                
            }
            // If any error in the object sent then return bad requested 
            catch (Exception e) {
                logger.error("Bad Request: Invalid User object");
                return ResponseEntity.badRequest().build();
            }

            }
            // If authorisation fails return Unauthorized
            else{
                logger.error("Unauthorised access");
                return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
            }
        }
        // If authorization header is not present then return bad request 
        else {
            logger.error("Bad Request: No Auth Header");
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
    
       
        
    }
     
    // Create a user
    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestParam Map<String,String> params, @RequestBody(required = false) String body, @RequestHeader(required = false) HttpHeaders header) {
        
        // If parameters are present or if there is not body send  Bad Request
        if ((params.size() > 0) || body == null) {
            logger.error("Bad Request: Request contains body");
            return ResponseEntity.badRequest().build();
        }
        // If there is an authorisation header return bad request
        if(header.getFirst("authorization") != null){

            logger.error("Bad Request:Auth Header Present");
            return ResponseEntity.badRequest().build();
        }
        ObjectMapper mapper = getMapper();
        User newUser;
        VerifyUser vuser = new VerifyUser();

        try {
            // Map the data of the user recieved from the Json body to the Pojo
            newUser = mapper.readValue(body, User.class);
            // Check if the username received is valid
            if(!isEmailValid(newUser.getUsername())) {return ResponseEntity.badRequest().build();}
            // Call the service to add new user to database
            if(newUser.getAccountCreated() != null || newUser.getAccountUpdated()!=null) {
                logger.error("Bad Request");
                return ResponseEntity.badRequest().build();
            }
            userService.addUser(newUser);
            JSONObject js = jsonMapper(newUser);
            infoLogger.info("User:"+newUser.getUsername()+"successfuly added");
            
            vuser.setUsername(newUser.getUsername());
            vuser.setVerified(false);
            verifyUserService.addUser(vuser);
            infoLogger.info("User:"+newUser.getUsername()+"successfuly added to Verification table");

            // Publish message to GCP
            pubSubService.publishPubSubMessage(newUser.getUsername());
            
            // Return 201 when user successfully added

            return  ResponseEntity
                    .status(HttpStatusCode.valueOf(201))
                    .cacheControl(CacheControl.noCache())
                    .body(js.toString());
        } catch (Exception e) {

            logger.error("Bad Request");
            return ResponseEntity.badRequest().build();
        }
        
    }

    // Mapper function that returns a mapper from Jackason databind library
    public ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DateFormat format = DateFormat.getDateTimeInstance();
        mapper.setDateFormat(format);
        return mapper;
    }


    // Function that maps the user object to a Json object
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

    //  Validates the email address.
    public boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }

    
}
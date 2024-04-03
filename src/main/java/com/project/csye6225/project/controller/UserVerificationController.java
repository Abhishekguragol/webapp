package com.project.csye6225.project.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.project.csye6225.project.service.VerifyUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/v1")
public class UserVerificationController {

    @Autowired
    private VerifyUserService verifyUserService;

    @GetMapping("/verify/{id}")
    public  ResponseEntity<Object> verifyUser(@RequestParam Map<String,String> params, @PathVariable String id) {
        
        // Logger for errors that occur in application
        Logger logger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_ERROR");

        // Logger for logging  information about requests made by users
        Logger infoLogger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_INFO");
        if(!(params.size() > 0) || id != null){
            infoLogger.info("Verifying user status");
            if(verifyUserService.updateStatus(id)){
                infoLogger.info("User status: Verified");
                return  ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .cacheControl(CacheControl.noCache())
                    .body("User Verified Succeffully");
            }
            else{
                infoLogger.info("Link status: Link Expired");
                return  ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .cacheControl(CacheControl.noCache())
                    .body("Link Expired");
            }
        }
        else{
            if(params.size() > 0){
                logger.error("Bad request : From verification link ");
            }
            else{
                logger.error("Bad request : Username not present ");
            }
            return ResponseEntity.badRequest().build();
            
        }
    }
    
    
}

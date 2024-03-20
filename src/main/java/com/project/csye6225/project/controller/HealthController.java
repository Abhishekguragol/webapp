package com.project.csye6225.project.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.csye6225.project.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/healthz")
public class HealthController {

    private final DataSource source;

    public HealthController(DataSource source){
        this.source = source;
    }
    
    @Autowired UserService userService;

    Logger errorLogger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_ERROR");
    Logger infoLogger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_INFO");

    @GetMapping()
    public ResponseEntity<Object> healthCheck(@RequestParam Map<String,String> params, @RequestBody(required = false) String body){

        
        // Return HTTP 400 Bad Request if the request has a payload
        if ((params.size() > 0) || body != null) {
            errorLogger.error("Bad request: Request body not null");
            return ResponseEntity.badRequest().build();
        }
            // Check database connectivity
            try (Connection con = source.getConnection()) {
                infoLogger.info("Database  connection established successfully.");
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .build();
            }
            catch(SQLException e){
                errorLogger.error("Failed to establish database  connection");
                return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .cacheControl(CacheControl.noCache())
                    .build();
            }
        
    }

    @RequestMapping("**")
    public ResponseEntity<Object> handleAllOtherRequest() {
        errorLogger.error("Invalid  endpoint requested");
        return  ResponseEntity
                .status(HttpStatusCode.valueOf(405))
                .cacheControl(CacheControl.noCache())
                .build();
    }
    
    
    
}

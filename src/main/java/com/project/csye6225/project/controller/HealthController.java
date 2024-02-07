package com.project.csye6225.project.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.csye6225.project.pojo.User;
import com.project.csye6225.project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/healthz")
public class HealthController {

    private final DataSource source;

    public HealthController(DataSource source){
        this.source = source;
    }
    
    @Autowired UserService userService;

    @GetMapping()
    public ResponseEntity<Object> healthCheck(@RequestParam Map<String,String> params, @RequestBody(required = false) String body){

        
        // Return HTTP 400 Bad Request if the request has a payload
        if ((params.size() > 0) || body != null) {
            return ResponseEntity.badRequest().build();
        }
            // Check database connectivity
            try (Connection con = source.getConnection()) {
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .build();
            }
            catch(SQLException e){
                return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .cacheControl(CacheControl.noCache())
                    .build();
            }
        
    }

    @RequestMapping("**")
    public ResponseEntity<Object> handleAllOtherRequest() {
        return  ResponseEntity
                .status(HttpStatusCode.valueOf(405))
                .cacheControl(CacheControl.noCache())
                .build();
    }
    
    
    
}

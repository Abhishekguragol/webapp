package com.project.csye6225.project.service;

import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.csye6225.project.pojo.VerifyUser;
import com.project.csye6225.project.repository.VerifyUserRepository;

@Service
public class VerifyUserService {

    @Autowired
    private VerifyUserRepository verifyUserRepository;

    // Logger for errors that occur in application
    Logger logger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_ERROR");

    // Logger for logging  information about requests made by users
    Logger infoLogger = (Logger) LogManager.getLogger("WEBAPP_LOGGER_INFO");

    public void addUser(VerifyUser user){
        verifyUserRepository.save(user);
    }

    public VerifyUser getByName(String username){
        VerifyUser fetchedUser = verifyUserRepository.findByUsername(username);
        return fetchedUser;
    }

    public boolean updateStatus(String username) {
    
        VerifyUser user = getByName(username);

        Instant userInstant = user.getEmailSent();
        Instant currentInstant = Instant.now();

        Duration duration = Duration.between(userInstant, currentInstant);

        long differenceInMinutes = Math.abs(duration.toMinutes());

        if(differenceInMinutes <= 2) {
            user.setVerified(true);
            verifyUserRepository.save(user);
            infoLogger.info("User :"+username+"  has been verified successfully.");
            return true;
        } else {
            logger.error("Verification link timed out for user: "+username);
        	System.out.println("Not verified");
        }

        return false;

    }
}

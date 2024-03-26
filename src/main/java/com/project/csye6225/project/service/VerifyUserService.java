package com.project.csye6225.project.service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;

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

    public void updateStatus(String username) {
    
        VerifyUser user = getByName(username);

        Calendar cal = Calendar.getInstance();
        Integer min = cal.get(Calendar.MINUTE);

        Instant t = user.getEmailSent();
        Integer sentMin = t.atZone(ZoneOffset.UTC).getMinute();

        if(sentMin - min < 2){
            user.setVerified(true);
            verifyUserRepository.save(user);
            infoLogger.info("User :"+username+"  has been verified successfully.");
        } else{
            logger.error("Verification link timed out for user: "+username);
        	System.out.println("Not verified");
        }
    }
}

package com.project.csye6225.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.csye6225.project.pojo.User;
import com.project.csye6225.project.repository.UserRepository;
import com.project.csye6225.project.utils.Security;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user){
        // Adding a new Instance of password encoder
        PasswordEncoder en = new Security().encoder();
        user.setPassword(en.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean userLoginAuth(User user){

        // Logic to fetch user based on the username
        User fetched = getByName(user.getUsername());
        String hashedPassword = fetched.getPassword();
        // Checks if the passwords match
        PasswordEncoder en = new Security().encoder();
        if(en.matches(user.getPassword(), hashedPassword)){
            return true;
        };

        return false;

    }

    public User getByName(String username){
        User fetchedUser = userRepository.findByUsername(username);
        return fetchedUser;
    }
    
}
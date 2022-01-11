package com.post.Blogdo.api;

import com.post.Blogdo.Models.UserDetails;
import com.post.Blogdo.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserRest {
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/api/signup")
    public String saveUserDetails(@Valid @RequestBody UserDetails userDetails)  {


        UserDetails foundUser = userRepo.findByUsername(userDetails.getUsername());


        if (foundUser != null) {
            return "user already exists";

        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encodedPassword);
        userDetails.setAuthorities("USER");
        userRepo.save(userDetails);
        return "registration Succesful";
    }
}

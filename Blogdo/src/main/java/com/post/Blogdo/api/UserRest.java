package com.post.Blogdo.api;

import com.post.Blogdo.Jwt.JwtUtil;
import com.post.Blogdo.Models.UserDetails;
import com.post.Blogdo.Repos.UserRepo;
import com.post.Blogdo.dto.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/login")
    public String AuthnticateUser(@RequestBody UserAuthentication authRequest) throws Exception {

        String pw_hash = BCrypt.hashpw(authRequest.getPassword(),BCrypt.gensalt());
        System.out.println(""+authRequest.getPassword());
        System.out.println("it is working");
        System.out.println(""+authRequest.getUserName());
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("invalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }

    @PostMapping("/api/signup")
    public String saveUserDetails(@Valid @RequestBody UserDetails userDetails)  {
        UserDetails foundUser = userRepo.findByUsername(userDetails.getUsername());
        if (foundUser != null) {
            return "user already exists";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encodedPassword);
        userDetails.setAuthorities("ROLE_USER");
        userRepo.save(userDetails);
        return "registration Successful";
    }
}

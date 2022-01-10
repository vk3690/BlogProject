package com.post.Blogdo.Controller;


import com.post.Blogdo.Models.UserDetails;
import com.post.Blogdo.Repos.UserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserDetails userDetails;

    @RequestMapping("/signup")
    public String userDatails(Model model)
    {
        model.addAttribute("UserDetails",userDetails);
        return "UserRegister.html";
    }
    @RequestMapping("/saveregister")
    public String saveUserDetails(@Valid UserDetails userDetails, BindingResult results)
    {
      UserDetails foundUser= userRepo.findByUsername(userDetails.getUsername());
        if (foundUser!=null ||results.hasErrors()) {
            return "redirect:"+"/signup";

        }
        System.out.println("ghh jnkm,"+userDetails.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encodedPassword);
        userDetails.setAuthorities("USER");
        System.out.println("ghh jnkm,"+userDetails.getPassword()+"     "+userDetails.getAuthorities());
        userRepo.save(userDetails);
        return "redirect:"+"/login";
    }
    @RequestMapping("/login")
    public String userLogin(Model model) {
        return "UserLogin.html";
    }

}

package com.post.Blogdo.Security;

import com.post.Blogdo.Models.UserDetails;
import com.post.Blogdo.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        System.out.println("in user deatails ");
        System.out.println("in user deatails jkm, kl,.adsklckl; "+username);

        UserDetails user= userRepo.findByUsername(username);

        if(user==null)
            throw new UsernameNotFoundException("user not found");
        return new MyUserDetails(user);
    }
}

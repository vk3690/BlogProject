package com.post.Blogdo.Security;

import com.post.Blogdo.Models.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private UserDetails user;

    public MyUserDetails(UserDetails user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        System.out.println("username");
        System.out.println(user.getAuthorities()+"fgcghvjknlmcxfgcvhjn,m");
        System.out.println("");
        list.add(new SimpleGrantedAuthority("ROLE_" + user.getAuthorities()));
       return list;
        }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    }


package com.post.Blogdo.Repos;

import com.post.Blogdo.Models.UserDetails;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserDetails,Integer> {

    UserDetails findByUsername(String username);




}

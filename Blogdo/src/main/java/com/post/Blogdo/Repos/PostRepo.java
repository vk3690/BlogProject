package com.post.Blogdo.Repos;


import com.post.Blogdo.Models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post,Integer> {
	
public Page<Post> findAll(Pageable pages);


    @Query(value= "select post.username from post",nativeQuery = true)
    ArrayList<String> findAllUsername();


    @Query(value = "select * from (Select * from post where post.id in :listOfUsernames)" +
            " as foo where foo.username=:username",nativeQuery = true)
    List<Post> findUsername(ArrayList<Integer> listOfUsernames, String username);

    ArrayList<Post> findByTitle(String searchKey);

    @Query(value = "select post.id from post where post.title=:searchKeyword or" +
            " post.username=:searchKeyword",nativeQuery = true)
    ArrayList<Integer> searchPostIdsWithAuthorAndTitle(String searchKeyword);

    @Query(value = "select foo.id from (select * from post where post.id in :allIds) as foo where " +
            "foo.username in :filterAuthors",nativeQuery = true)
    ArrayList<Integer> filterByAuthorName(String[] filterAuthors, ArrayList<Integer> allIds);


    Page<Post> findByIdIn(List<Integer> alis, Pageable page);
    @Query(value = "select post.id from post",nativeQuery = true)
    ArrayList<Integer> findByListOfIds();


    @Query(value= "select post.id from post  where post.updated_at BETWEEN :startDate AND :endDate",nativeQuery = true)
    ArrayList<Integer> findIdsBetweenDates(Date startDate,Date endDate);


}


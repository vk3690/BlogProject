package com.post.Blogdo.Repos;

import com.post.Blogdo.Models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
@Repository
public interface TagRepo extends JpaRepository<Tag,Integer> {


    @Query(value = "select tag.post_id from tag where tag.tag=:tag",nativeQuery = true)
    ArrayList<Integer> searchPostIdsWithTags(String tag);

    @Query(value = "select foo.post_id from (select * from tag where tag.post_id in :allIds) as foo" +
            " where  foo.tag in :filterTags",nativeQuery = true)
    ArrayList<Integer> filterByTags(String[] filterTags, ArrayList<Integer> allIds) ;

    @Query(value = "select tag.tag from tag",nativeQuery = true)
    Set<String> filterAllTags();
}

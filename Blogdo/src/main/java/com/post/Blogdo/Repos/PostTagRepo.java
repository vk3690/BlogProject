package com.post.Blogdo.Repos;

import com.post.Blogdo.Models.PostTag;
import com.post.Blogdo.Models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostTagRepo extends JpaRepository<PostTag,Integer>{

    Optional<PostTag> findByPostId(Integer blogId);
}

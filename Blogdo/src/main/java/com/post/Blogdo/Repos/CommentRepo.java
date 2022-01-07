package com.post.Blogdo.Repos;


import com.post.Blogdo.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {





	//@Query("SELECT u FROM comments WHERE u.blog_id=?1")
	//public ArrayList<CommentModel> getCommentsByBlogId(Integer id);

}


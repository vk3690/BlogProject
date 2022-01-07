package com.post.Blogdo.Service;


import com.post.Blogdo.Models.Comment;
import com.post.Blogdo.Models.Post;
import com.post.Blogdo.Repos.CommentRepo;
import com.post.Blogdo.Repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {


	@Autowired
	private CommentRepo commentRepo;
	@Autowired
	private Comment commentModel;
	@Autowired
	private PostRepo postRepo;
	

	public Optional<Comment> editComment(Integer id)
	{
		return commentRepo.findById(id);
	}
	
	public void postComment(String comment, String username,String mail,Integer postId) {

		Optional<Post> post=postRepo.findById(postId);
		Post commentedPost=  post.get();
		commentModel.setPost(commentedPost);
		commentModel.setUsername(username);
		commentModel.setCreatedAt(new Date());
		commentModel.setUpdateAt(new Date());
		commentModel.setMail(mail);
		commentModel.setUserComment(comment);
		commentRepo.save(commentModel);
		//return bolgIdAndUsernames[1];
	}
	
	
	public Integer updateComment(Integer id, String updatedComment) {

			Optional<Comment> commentModel=commentRepo.findById(id);
			Comment commentModelValues=commentModel.get();

			Post postId=commentModelValues.getPost();
			commentModelValues.setUserComment(updatedComment);
			commentModelValues.setUpdateAt(new Date());
			commentRepo.save(commentModelValues);
			return postId.getId();

	}
	public Integer deleteComment(Integer id)
	{
		Optional<Comment> commentModel=commentRepo.findById(id);
		Comment commentModelValues=commentModel.get();

		Integer postId=commentModelValues.getPost().getId();
		System.out.println();
		System.out.println(postId+"   hjkn");
		System.out.println();
		commentRepo.deleteById(id);
		return postId;
	}





}

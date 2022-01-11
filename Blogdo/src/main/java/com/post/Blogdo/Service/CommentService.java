package com.post.Blogdo.Service;


import com.post.Blogdo.Models.Comment;
import com.post.Blogdo.Models.Post;
import com.post.Blogdo.Repos.CommentRepo;
import com.post.Blogdo.Repos.PostRepo;
import com.post.Blogdo.Security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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


	public Optional<Comment> editComment(Integer id) {
		return commentRepo.findById(id);
	}

	public void postComment(String comment, String username, String mail, Integer postId) {

		Optional<Post> post = postRepo.findById(postId);
		Post commentedPost = post.get();
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

		Optional<Comment> commentModel = commentRepo.findById(id);
		Comment commentModelValues = commentModel.get();

		Post postId = commentModelValues.getPost();
		commentModelValues.setUserComment(updatedComment);
		commentModelValues.setUpdateAt(new Date());
		commentRepo.save(commentModelValues);
		return postId.getId();

	}

	public Integer deleteComment(Integer id) {
		Optional<Comment> commentModel = commentRepo.findById(id);
		Comment commentModelValues = commentModel.get();

		Integer postId = commentModelValues.getPost().getId();
		System.out.println();
		System.out.println(postId + "   hjkn");
		System.out.println();
		commentRepo.deleteById(id);
		return postId;
	}


	public String deleteApiComment(Integer commentId) {

		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<Comment> comment = commentRepo.findById(commentId);
		Comment commentToDelete = comment.get();
		if (commentToDelete.getPost().getUsername().equals(userDetails.getUsername()) ||
				commentToDelete.getUsername().equals(userDetails.getUsername())) {
			commentRepo.deleteById(commentId);
			return "comment deleted";

		} else {
			return "not authorised to delete";
		}
	}

	public String updateApiComment(Integer commentId, String updatedComment) {

		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<Comment> comment = commentRepo.findById(commentId);
		Comment commentToUpdate = comment.get();
		if (commentToUpdate.getPost().getUsername().equals(userDetails.getUsername()) ||
				commentToUpdate.getUsername().equals(userDetails.getUsername())) {
				commentToUpdate.setUserComment(updatedComment);
				commentToUpdate.setUpdateAt(new Date());
				commentRepo.save(commentToUpdate);
				return "comment updated succesfully";
		}
		else{
			return "not authorized to update comment";
		}

	}
}

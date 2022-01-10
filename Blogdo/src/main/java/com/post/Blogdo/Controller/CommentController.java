package com.post.Blogdo.Controller;


import com.post.Blogdo.Models.Comment;
import com.post.Blogdo.Service.PostService;
import com.post.Blogdo.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
public class CommentController {
	
	
	@Autowired
	private PostService blogService;
	@Autowired
	private CommentService commentService;

	 @PostMapping("/postComment")
	 public String postComment(@RequestParam("comment") String comment,@RequestParam("username") String username,
			 @RequestParam("mail") String mail, @RequestParam("blogId") Integer blogId,RedirectAttributes redirectAttributes)
	 {
		 
		 commentService.postComment(comment,username,mail,blogId);
		 redirectAttributes.addAttribute("startpage",0);
		 redirectAttributes.addAttribute("blogId",blogId);
		return  "redirect:"+"/readblog";
	 }
	 
	 @RequestMapping("/editcomment")
	 public String editComment(@RequestParam("id") Integer id,Model model)
	 {
		Optional<Comment> editableComment=commentService.editComment(id);
		// System.out.println(editableComment);
			model.addAttribute("commentForEdit",editableComment.get());
		 return "editcomment.html";
	 }
	 
	 @PostMapping("/updatecomment")
	 public String updateComment(@RequestParam("editedcommentid") Integer editedCommentId,@RequestParam("editedcomment") String updatedComment,
			 RedirectAttributes redirectAttributes)
	 {
		 System.out.print("in update");
		Integer postId =commentService.updateComment(editedCommentId,updatedComment);
		redirectAttributes.addAttribute("blogId",postId);
		
		 return "redirect:"+"/readblog";
	 }

	 @PostMapping("/deletecomment")
	 public String deleteComment(@RequestParam("id") Integer editedCommentId,RedirectAttributes redirectAttributes)
	 {
		
		 Integer postId =commentService.deleteComment(editedCommentId);
		redirectAttributes.addAttribute("blogId",postId);
		
		 return "redirect:"+"/readblog";
	 }

}

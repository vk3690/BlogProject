package com.post.Blogdo.api;

import com.post.Blogdo.Models.Comment;
import com.post.Blogdo.Models.UserDetails;
import com.post.Blogdo.Repos.CommentRepo;
import com.post.Blogdo.Security.MyUserDetails;
import com.post.Blogdo.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CommentRest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepo commentRepo;

    @PostMapping("/api/postComment")
    public String postComment( @RequestBody Comment comment, @RequestParam(name="blogId") Integer blogId)
    {

        commentService.postComment(comment.getUserComment(),comment.getUsername(),comment.getMail(),blogId);
        return  "comment posted sucessful";
    }

    @PostMapping("/api/updateComment")
    public String updateComment(@RequestParam(name="updatedComment") String updatedComment,
                                 @RequestParam(name="commentId") Integer commentId)
    {
      return   commentService.updateApiComment(commentId,updatedComment);
    }

    @DeleteMapping("/api/deleteComment")
    public void deleteComment(@RequestParam(name="commentId") Integer commentId, HttpServletResponse response)
            throws IOException {

       String responses= commentService.deleteApiComment(commentId);
        response.sendRedirect("/api/dash");
    }
}

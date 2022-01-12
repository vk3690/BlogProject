package com.post.Blogdo.api;

import com.post.Blogdo.Jwt.JwtUtil;
import com.post.Blogdo.Models.Post;
import com.post.Blogdo.Repos.PostRepo;
import com.post.Blogdo.Security.MyUserDetails;
import com.post.Blogdo.Service.PostService;
import com.post.Blogdo.Service.TagService;
import com.post.Blogdo.dto.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostRest {

    @Autowired
    private TagService tagService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/api/login")
    public String AuthnticateUser(@RequestBody UserAuthentication authRequest) throws Exception {

        String pw_hash = BCrypt.hashpw(authRequest.getPassword(),BCrypt.gensalt());
        System.out.println(""+authRequest.getPassword());
        System.out.println("it is working");
        System.out.println(""+authRequest.getUserName());
        try {

           authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword())
           );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }



    @GetMapping("/api/dash")
    public List<Post> redirectToDashboard(@RequestParam(name = "keywordToSearch", defaultValue = "") String keywordToSearch,
                                          @RequestParam(name  = "tag", defaultValue = "") String tag ,
                                          @RequestParam(name = "author", defaultValue = "") String author,
                                          @RequestParam(name  = "pageNo", defaultValue = "0") Integer pageNo,
                                          @RequestParam(name ="from",defaultValue="2021-12-25") Date startDate,
                                          @RequestParam(name ="to",defaultValue="2022-12-25") Date endDate,
                                          @RequestParam(name  = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted)
    {

        List<Post> allBlogs=postService.testQ1(keywordToSearch,tag,author,pageNo,startDate,endDate,keywordToBeSorted);

        return allBlogs;
    }

    @RequestMapping("api/readblog")
    public Post readBlog(@RequestParam(name="blogId") Integer blogId, HttpServletResponse response)
    {
        Optional<Post> blogPost=postRepo.findById(blogId);
        Post blogPostToDisplay=blogPost.get();

        return blogPostToDisplay;
    }

    @RequestMapping(path="/api/writeBlog",method = RequestMethod.POST)
    public String saveBlog(@RequestBody Post post,@RequestParam(name="tags") String tags) {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        post.setUsername(myUserDetails.getUsername());
        if(myUserDetails.getUsername()!=null) {
            tagService.storeTagsOfBlogs(post, tags);
            postService.saveBlogPost(post);
            return "blog is posted";
        }
        //System.out.println(tags);
        return "login to post";
    }


    @PostMapping("/api/updateBlogPost")
    public String updateComment(@RequestBody Post post, @RequestParam("postId") Integer postId,
                                      @RequestParam("editedTags") String editedtags)
    {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if(myUserDetails.getUsername().equals(post.getUsername()) ||
                myUserDetails.getAuthorities().contains("ROLE_ADMIN")) {
            postService.updatePost(postId, post.getTitle(), post.getBlogPost(), editedtags);
            return "blog updated successfully";
        }
        else {
            //tagService.updateTagsOfBlog(postId,editedtags);
            return "not authorized to update";
        }
    }

        @DeleteMapping("/api/deleteBlogPost")
    public String deleteBlog(@RequestParam(name="blogId") Integer blogId)
    {

       return postService.deletePostFromApi(blogId);

    }






}





package com.post.Blogdo.api;

import com.post.Blogdo.Jwt.JwtUtil;
import com.post.Blogdo.Models.Post;
import com.post.Blogdo.Repos.PostRepo;
import com.post.Blogdo.Service.PostService;
import com.post.Blogdo.Service.TagService;
import com.post.Blogdo.dto.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostRest {

    @Autowired
    private TagService tagService;
    @Autowired
    private PostService blogService;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/api/login")
    public String generateTokens(@RequestBody UserAuthentication authRequest) throws Exception {

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

        List<Post> allBlogs=blogService.testQ1(keywordToSearch,tag,author,pageNo,startDate,endDate,keywordToBeSorted);

        return allBlogs;
    }

    @GetMapping("/api/dash2")
    public List<Post> dashBoard(Model model, @RequestParam(value = "keywordToSearch", defaultValue = "") String keywordToSearch,
                                @RequestParam(value = "tag", defaultValue = "") String tag ,
                                @RequestParam(value = "author", defaultValue = "") String author,
                                @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                @RequestParam(value="from",defaultValue="2021-12-25") Date from,
                                @RequestParam(value="to",defaultValue="2022-12-25") Date to,
                                @RequestParam(value = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted,
                                @RequestParam("blogPosts") List<Post> pageOfBlogs)
    {
//        model.addAttribute("keywordToSearch",keywordToSearch);
//        model.addAttribute("from",from);
//        model.addAttribute("author",author);
//        model.addAttribute("tag",tag);
//        model.addAttribute("to",to);
//        model.addAttribute("keywordToBeSorted",keywordToBeSorted);
//        ArrayList<String> tags=tagService.getTags();
//        model.addAttribute("pageNo",pageNo);
        ArrayList<String> authors=blogService.getAuthors();
        model.addAttribute("authors",authors);

        // System.out.println(tags.size());


        return pageOfBlogs;
    }
    @RequestMapping("api/readblog")
    public Post readBlog(@RequestParam(name="blogId") Integer blogId)
    {
        Optional<Post> blogPost=postRepo.findById(blogId);
        Post blogPostToDisplay=blogPost.get();

        return blogPostToDisplay;
    }

    @RequestMapping(path="/api/writeBlog",method = RequestMethod.POST)
    public Post saveBlog(@RequestBody Post post,@RequestParam(name="tags") String tags) {
//        redirectAttributes.addAttribute("startpage", 0);
        blogService.saveBlogPost(post);
        tagService.storeTagsOfBlogs(post, tags);
        System.out.println(tags);
        return post;
    }


    @PostMapping("/api/updateBlogPost")
    public String updateComment(@RequestBody Post post, @RequestParam("postId") Integer postId,
                                      @RequestParam("editedTags") String editedtags)
    {
        blogService.updatePost(postId,post.getTitle(),post.getBlogPost(),editedtags);
        //tagService.updateTagsOfBlog(postId,editedtags);
        return "blog updated";
    }

    @DeleteMapping("/api/deleteBlogPost")
    public String deleteBlog(@RequestParam("blogId") Integer blogId)
    {
        blogService.deleteBlog(blogId);
        return "blog deleted";
    }


    @RequestMapping("/api/filter")
    public List<Post> filterTags(@RequestParam(name = "keywordToSearch", defaultValue = "") String keywordToSearch,
                                 @RequestParam(name  = "tag", defaultValue = "") String tag ,
                                 @RequestParam(name = "author", defaultValue = "") String author,
                                 @RequestParam(name  = "pageNo", defaultValue = "0") Integer pageNo,
                                 @RequestParam(name ="from",defaultValue="2021-12-25") Date startDate,
                                 @RequestParam(name ="to",defaultValue="2022-12-25") Date endDate,
                                 @RequestParam(name  = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted)

    {

        pageNo=0;
        List<Post> allBlogPosts=blogService.testQ1(keywordToSearch,tag,author,pageNo,startDate,endDate,keywordToBeSorted);
        return  allBlogPosts;
}

    @GetMapping("/api/searchby")
    public List<Post> searchBy(@RequestParam(name = "keywordToSearch", defaultValue = "") String keywordToSearch,
                               @RequestParam(name  = "tag", defaultValue = "") String tag ,
                               @RequestParam(name = "author", defaultValue = "") String author,
                               @RequestParam(name  = "pageNo", defaultValue = "0") Integer pageNo,
                               @RequestParam(name ="from",defaultValue="2021-12-25") Date startDate,
                               @RequestParam(name ="to",defaultValue="2022-12-25") Date endDate,
                               @RequestParam(name  = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted)

    {
        System.out.println(keywordToSearch+"  hjbnkm,  ");
        pageNo=0;
        List<Post> allBlogPosts=blogService.testQ1(keywordToSearch,tag,author,pageNo,startDate,endDate,keywordToBeSorted);

        return allBlogPosts;
    }

}




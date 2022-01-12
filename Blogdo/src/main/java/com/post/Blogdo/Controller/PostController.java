package com.post.Blogdo.Controller;


import com.post.Blogdo.Models.Post;
import com.post.Blogdo.Repos.PostRepo;
import com.post.Blogdo.Service.PostService;
import com.post.Blogdo.Service.CommentService;
import com.post.Blogdo.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class PostController {
    @Autowired
    private PostService blogService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostRepo postRepo;

    @PostMapping("/postblog")
    public String writeBlog() {
        return "writeblog.html";
    }

    @PostMapping("/saveBlog")
    public String saveBlog(Post post, @RequestParam("tags") String tags,
                           RedirectAttributes redirectAttributes) {
        blogService.saveBlogPost(post);
        redirectAttributes.addAttribute("startpage", 0);

        tagService.storeTagsOfBlogs(post, tags);

        return "redirect:" + "/";
    }


    @RequestMapping("/sortby")
    public String sortBy(@RequestParam(value = "keywordToSearch", defaultValue = "") String keywordToSearch,
                         @RequestParam(value = "tag", defaultValue = "") String tag ,
                         @RequestParam(value = "author", defaultValue = "") String author,
                         @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                         @RequestParam(value="from",defaultValue="2021-12-25") Date startDate,
                         @RequestParam(value="to",defaultValue="2022-12-25") Date endDate,
                         @RequestParam(value = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted,
                         RedirectAttributes redirectAttributes)
    {
        if(keywordToBeSorted.equals("date"))
        {
            keywordToBeSorted="updatedAt";
        }
        else if(keywordToBeSorted.equals("author"))
        {
            keywordToBeSorted="username";

        }

        redirectAttributes.addAttribute("keywordToSearch",keywordToSearch);
        redirectAttributes.addAttribute("tag",tag);
        redirectAttributes.addAttribute("author",author);
        redirectAttributes.addAttribute("pageNo",0);
        redirectAttributes.addAttribute("from",startDate);
        redirectAttributes.addAttribute("to",endDate);
        redirectAttributes.addAttribute("sortOrder",keywordToBeSorted);
        return "redirect:"+"/";
    }

    @RequestMapping("/readblog")
    public String readBlog(@RequestParam("blogId") Integer blogId,Model model)
    {
        Optional<Post> blogPost=postRepo.findById(blogId);
        Post blogPostToDisplay=blogPost.get();
       // ArrayList<Comment> commentsOnBlogPost=commentService.getCommentsById(blogId);
        System.out.println();
        System.out.println(blogPostToDisplay.getComment().size());
        System.out.println();
        //String blogTags=blogService.blogTags(blogId);
        model.addAttribute("comments",blogPostToDisplay.getComment());
        model.addAttribute("blogDao",blogPostToDisplay);

        	model.addAttribute("blogTags",blogPostToDisplay.getTag().getTag());
        return "Readblog.html";
    }
    @RequestMapping("/deleteblog")
    public String deleteBlog(@RequestParam("blogId") Integer blogId)
    {
        blogService.deleteBlog(blogId);
        return "redirect:"+"/";
    }


    @PostMapping("/editblog")
    public String editBlog(@RequestParam("blogId") Integer blogId,Model model)
    {

        Optional<Post> postToBeEdited=blogService.getBlogToBeEdited(blogId);
        String tagsOfBlogPost= tagService.getTagsOfBlog(blogId);
        System.out.println("");
        System.out.println(postToBeEdited.get().getTitle());
        System.out.println("");
        model.addAttribute("postData",postToBeEdited.get());
       model.addAttribute("tags",tagsOfBlogPost);
        return "editblog.html";
    }

    @PostMapping("/updateBlogPost")
    public String updateComment(@RequestParam("editedblog") String editedBlog,
                                @RequestParam("editedtitle") String editedTitle,@RequestParam("blogId") Integer postId,
                                @RequestParam("editedtags") String editedtags,
                                RedirectAttributes redirectAttributes)
    {
        	blogService.updatePost(postId,editedTitle,editedBlog,editedtags);
       // tagService.updateTagsOfBlog(postId,editedtags);
        return "redirect:"+"/";
    }




    @RequestMapping("/")
    public String redirectToDashboard(@RequestParam(value = "keywordToSearch", defaultValue = "") String keywordToSearch,
                                      @RequestParam(value = "tag", defaultValue = "") String tag ,
                                      @RequestParam(value = "author", defaultValue = "") String author,
                                      @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                      @RequestParam(value="from",defaultValue="2021-12-25") Date startDate,
                                      @RequestParam(value="to",defaultValue="2022-12-25") Date endDate,
                                      @RequestParam(value = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted,
                                      RedirectAttributes redirectAttributes)
    {
        System.out.println("  kkl533"+tag);
        List<Post> allBlogs=blogService.testQ1(keywordToSearch,tag,author,pageNo,startDate,endDate,keywordToBeSorted);
        redirectAttributes.addAttribute("keywordToSearch",keywordToSearch);
        redirectAttributes.addAttribute("tag",tag);
        redirectAttributes.addAttribute("author",author);
        redirectAttributes.addAttribute("pageNo",pageNo);
        redirectAttributes.addAttribute("blogPosts",allBlogs);

        return "redirect:" + "/dashboard";
    }

    @RequestMapping("/dashboard")
    public String dashBoard(Model model,@RequestParam(value = "keywordToSearch", defaultValue = "") String keywordToSearch,
                            @RequestParam(value = "tag", defaultValue = "") String tag ,
                            @RequestParam(value = "author", defaultValue = "") String author,
                            @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                            @RequestParam(value="from",defaultValue="2021-12-25") Date from,
                            @RequestParam(value="to",defaultValue="2022-12-25") Date to,
                            @RequestParam(value = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted,
                            @RequestParam("blogPosts") List<Post> pageOfBlogs)
    {
        model.addAttribute("keywordToSearch",keywordToSearch);
        model.addAttribute("from",from);
        model.addAttribute("author",author);
        model.addAttribute("tag",tag);
        model.addAttribute("to",to);
        model.addAttribute("keywordToBeSorted",keywordToBeSorted);
        ArrayList<String> tags=tagService.getTags();
        model.addAttribute("pageNo",pageNo);
        ArrayList<String> authors=blogService.getAuthors();
        model.addAttribute("authors",authors);
        ArrayList<String> sortOrderList=new ArrayList<String>();
        sortOrderList.add("date");
        sortOrderList.add("author");
        sortOrderList.add("title");
        model.addAttribute("tags",tags);
       // System.out.println(tags.size());

        model.addAttribute("allblogs",pageOfBlogs);
        model.addAttribute("sortOrderList",sortOrderList);
        return "Dashboard.html";
    }

    @RequestMapping("/filter")
    public String filterTags(@RequestParam(value="tag",defaultValue = "") String tag,
                             @RequestParam(value="from",defaultValue="2021-12-25") Date from,
                             @RequestParam(value="to",defaultValue="2022-12-25") Date to,
                             @RequestParam("keywordToSearch") String keywordToSearch,
                             @RequestParam(value = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted,
                             @RequestParam(value="author",defaultValue = "") String author,
                             RedirectAttributes redirectAttributes)
    {
        System.out.println("");
        System.out.println(" vikas "+keywordToSearch+"    nwf f ");
        System.out.println("");

        redirectAttributes.addAttribute("keywordToSearch",keywordToSearch);
        redirectAttributes.addAttribute("tag",tag);
        redirectAttributes.addAttribute("author",author);
        redirectAttributes.addAttribute("pageNo",0);
        redirectAttributes.addAttribute("from",from);
        redirectAttributes.addAttribute("to",to);
        redirectAttributes.addAttribute("sortOrder",keywordToBeSorted);
        return "redirect:" + "/";
    }


    @RequestMapping("/searchby")
    public String searchBy(@RequestParam("keywordToSearch") String keywordToSearch,
//								   @RequestParam("tag") String tag,
//								   @RequestParam("author") String author,@RequestParam("pageNo") Integer pageNo,
                           RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addAttribute("keywordToSearch", keywordToSearch);
        //redirectAttributes.addAttribute("tags","");
        //redirectAttributes.addAttribute("authors","");
        System.out.println("");
        System.out.println("nm"+keywordToSearch);
        System.out.println();
        redirectAttributes.addAttribute("pageNo",0);
        return "redirect:" + "/";
    }
    @RequestMapping("/nextpage")
    public String nextpage(@RequestParam("keywordToSearch") String keywordToSearch,@RequestParam("tags") String tags,
                           @RequestParam("authors") String authors,@RequestParam("pageNo") Integer pageNo,
                           @RequestParam(value="from",defaultValue="2021-12-25") Date from,
                           @RequestParam(value="to",defaultValue="2022-12-25") Date to,
                           @RequestParam(value = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted,
                           RedirectAttributes redirectAttributes)
    {

        pageNo=pageNo+1;
        redirectAttributes.addAttribute("keywordToSearch",keywordToSearch);
        redirectAttributes.addAttribute("tags",tags);
        redirectAttributes.addAttribute("authors",authors);
        redirectAttributes.addAttribute("pageNo",pageNo);
        redirectAttributes.addAttribute("from",from);
        redirectAttributes.addAttribute("to",to);
        redirectAttributes.addAttribute("sortOrder",keywordToBeSorted);
        return "redirect:" + "/";
    }

    @RequestMapping("/prevpage")
    public String previouspage(@RequestParam("keywordToSearch") String keywordToSearch,@RequestParam("tags") String tags,
                           @RequestParam("authors") String authors,@RequestParam("pageNo") Integer pageNo,
                           @RequestParam(value="from",defaultValue="2021-12-25") Date from,
                           @RequestParam(value="to",defaultValue="2022-12-25") Date to,
                           @RequestParam(value = "sortOrder",defaultValue = "UpdatedAt") String keywordToBeSorted,
                           RedirectAttributes redirectAttributes)
    {
        pageNo=pageNo-1;
        redirectAttributes.addAttribute("keywordToSearch",keywordToSearch);
        redirectAttributes.addAttribute("tags",tags);
        redirectAttributes.addAttribute("authors",authors);
        redirectAttributes.addAttribute("pageNo",pageNo);
        redirectAttributes.addAttribute("from",from);
        redirectAttributes.addAttribute("to",to);
        redirectAttributes.addAttribute("sortOrder",keywordToBeSorted);
        return "redirect:" + "/";
    }
}






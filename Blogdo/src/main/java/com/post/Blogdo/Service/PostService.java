package com.post.Blogdo.Service;


import com.post.Blogdo.Models.Post;
import com.post.Blogdo.Models.PostTag;
import com.post.Blogdo.Repos.PostRepo;
import com.post.Blogdo.Repos.PostTagRepo;
import com.post.Blogdo.Repos.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private PostTagRepo postTagRepo;
    @Autowired
    private TagRepo tagRepo;

    public void saveBlogPost(Post blogModel) {
        Date date = new Date();
        //BlogModel blogModel=new BlogModel();
        String blog = blogModel.getBlogPost();
        String excerpt = "";
        String[] temp = blog.split(" ");
        for (int index = 0; index <= 25; index++) {
            excerpt = excerpt + " " + temp[index];
        }
        blogModel.setExcerpt(excerpt);
        blogModel.setCreatedAt(date);
        blogModel.setUpdatedAt(date);
        postRepo.save(blogModel);
    }


    public ArrayList<String> getAuthors() {
        ArrayList<String> allAuthors=  postRepo.findAllUsername();

        Set<String> setOfAuthors=new HashSet<>(allAuthors);
        allAuthors=new ArrayList<>(setOfAuthors);
        return allAuthors;
}
    public List<Post> testQ1(String searchKeyword, String tags, String author, Integer pageNo, Date startDate,
                             Date endDate, String keyWordToBeSorted) {
        ArrayList<Integer> allPostedBlogIds = new ArrayList<>();
        String defaultStartDate="2021-12-25";
        String deafalutEndDate="2022-12-25";
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        Date startDateDefault=null;
        Date endDateDefault=null;
        try {
             startDateDefault = formatter.parse(defaultStartDate);
            endDateDefault = formatter.parse(deafalutEndDate);
        }
        catch (Exception e)
        {

        }
//        ArrayList<Integer> allPostedBlogIds = postRepo.findIdsOfBetweenDates(startDate,endDate, allPostBlogIds);
        System.out.println("");
          System.out.println("vhjnkm,  "+!startDateDefault.equals(startDate)+"   hjn564545");
        System.out.println("");
        //   ArrayList<Integer> getIdsByDate=blogRepo.findIdsBetweenDates(startDate,endDate);
        // allPostedBlogIds.clear();
        //allPostedBlogIds.addAll(getIdsByDate);
        String[] tagsToFilter = tags.split(",");
        String[] AuthorsToFilter = author.split(",");
        if(!searchKeyword.equals(""))
        {
            ArrayList<Integer> searchid = postRepo.searchPostIdsWithAuthorAndTitle(searchKeyword);
            ArrayList<Integer> searchidtags=tagRepo.searchPostIdsWithTags(searchKeyword);
            allPostedBlogIds.clear();
            allPostedBlogIds.addAll(searchidtags);
            allPostedBlogIds.addAll(searchid);
        }
        else{
            allPostedBlogIds=postRepo.findByIds();
        }

    if(!startDate.equals(defaultStartDate) || !endDate.equals(deafalutEndDate))
    {
        allPostedBlogIds=postRepo.findIdsOfBetweenDates(startDate,endDate,allPostedBlogIds);
    }

        if(!AuthorsToFilter[0].equals("") || !tagsToFilter[0].equals(""))
        {
            ArrayList<Integer> searchAuthors= postRepo.filterByAuthorName(AuthorsToFilter,allPostedBlogIds);
            ArrayList<Integer> searchTags=tagRepo.filterByTags(tagsToFilter,allPostedBlogIds);
            allPostedBlogIds=searchTags;
            allPostedBlogIds.addAll(searchAuthors);
        }
        Pageable page = PageRequest.of(pageNo, 5, Sort.by(keyWordToBeSorted));
        Page<Post> pageOfBlogs = (Page<Post>) postRepo.findByIdIn(allPostedBlogIds,page);
        List<Post> blogPage= pageOfBlogs.getContent();

        return  blogPage;
    }
    public Optional<Post> getBlogToBeEdited(Integer blogId)
    {
        Optional<Post> blogModel= postRepo.findById(blogId);
        return blogModel;
    }
    public String updatePost(Integer blogId, String editedTitle, String editedBlog, String editedtags)
    {
        Optional<Post> getPostToUpdate= postRepo.findById(blogId);
        Post postValues =getPostToUpdate.get();
        postValues.setTitle(editedTitle);
        postValues.setBlogPost(editedBlog);
        PostTag postTag=postValues.getTag();
        postTag.setTag(editedtags);
        postTagRepo.save(postTag);
        return editedBlog;
    }

    public void deleteBlog(Integer blogId) {
        postRepo.deleteById(blogId);
    }

}
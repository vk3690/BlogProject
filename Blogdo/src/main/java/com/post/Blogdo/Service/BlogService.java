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

import java.util.*;

@Service
public class BlogService {
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
        ArrayList<Integer> allPostedBlogIds = postRepo.findIdsBetweenDates(startDate,endDate);
        System.out.println("");
        //   System.out.println("vhjnkm,  "+blogPage.size()+"   hjn564545");
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

        if(!AuthorsToFilter[0].equals("") || !tagsToFilter[0].equals(""))
        {
            ArrayList<Integer> searchAuthors= postRepo.filterByAuthorName(AuthorsToFilter,allPostedBlogIds);
            ArrayList<Integer> searchTags=tagRepo.filterByTags(tagsToFilter,allPostedBlogIds);
            allPostedBlogIds.clear();
            allPostedBlogIds.addAll(searchTags);
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

}
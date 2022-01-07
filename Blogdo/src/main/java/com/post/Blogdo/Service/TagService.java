package com.post.Blogdo.Service;


import com.post.Blogdo.Models.Post;
import com.post.Blogdo.Models.PostTag;
import com.post.Blogdo.Models.Tag;
import com.post.Blogdo.Repos.PostRepo;
import com.post.Blogdo.Repos.PostTagRepo;
import com.post.Blogdo.Repos.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagService {
    @Autowired
    public PostTagRepo postTagRepo;
    @Autowired
    private TagRepo tagRepo;
    @Autowired
    public PostRepo postRepo;

    public void storeTagsOfBlogs(Post post, String tags) {
        Date date = new Date();
        PostTag postTag = new PostTag();
        postTag.setCreatedAt(date);
        postTag.setUpdatedAt(date);
       // Optional<Post> post = postRepo.findById(postId);
        postTag.setPost(post);
        postTag.setTag(tags);
        postTagRepo.save(postTag);

        String[] tagss = tags.split(",");

        for(String temp:tagss)
        {
            Tag tag =new Tag();
            tag.setTag(temp);
            tag.setPostId(post.getId());
            tagRepo.save(tag);
        }

    }

    public String getTagsOfBlog(Integer blogId) {
        Optional<PostTag> tagsOfBlog = postTagRepo.findByPostId(blogId);
        return tagsOfBlog.get().getTag();
    }

    public ArrayList<String> getTags() {

        Set<String> tags=tagRepo.filterAllTags();
        ArrayList<String> listOfTags=new ArrayList<>(tags);
        return listOfTags;
    }


//    public void updateTagsOfBlogs(Integer postId, String editedtags) {
//
//        Optional<PostTag> getTagsOfBlogId=postTagRepo.findByPostId(postId);
//        for(PostTag row:getTagsOfBlogId)
//        {
//            postTagRepo.deleteById(row.getId());
//        }
//        String[] updatedTags=editedtags.split(",");
//        for(int i=0;i<updatedTags.length;i++)
//        {
//            PostTag updatedPostTags=new PostTag();
//            updatedPostTags.setBlogId(blogId);
//            updatedPostTags.setTag(updatedTags[i]);
//            postTagRepo.save(updatedPostTags);
//            }
//        }
}

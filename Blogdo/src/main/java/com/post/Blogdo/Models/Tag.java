package com.post.Blogdo.Models;


import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Component
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer postId;

private String tag;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }


public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}

public String getTag() {
	return tag;
}
public void setTag(String tag) {
	this.tag = tag;
}
}

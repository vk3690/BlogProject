package com.post.Blogdo.Models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.List;



@Entity
@Component
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	@Lob
	private String blogPost;
	@Lob
	private String excerpt;
	private Date createdAt;
    private String username;
    private Date updatedAt;
	@JsonManagedReference
	@OneToMany(mappedBy = "post",cascade =  CascadeType.ALL)
	private List<Comment> comment;
	@JsonManagedReference
	@OneToOne(mappedBy = "post",cascade = CascadeType.ALL)
	private PostTag tag;


	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public PostTag getTag() {
		return tag;
	}

	public void setTag(PostTag tag) {
		this.tag = tag;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date date) {
		this.updatedAt = (Date) date;
	}

	public void setCreatedAt(Date date) {
		this.createdAt = (Date) date;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBlogPost() {
		return blogPost;
	}

	public void setBlogPost(String blogPost) {
		this.blogPost = blogPost;
	}
	

}

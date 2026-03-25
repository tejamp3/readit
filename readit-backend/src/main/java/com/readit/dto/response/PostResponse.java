package com.readit.dto.response;

import com.readit.entity.Post.PostCategory;
import java.time.LocalDateTime;

public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private PostCategory category;
    private String authorUsername;
    private LocalDateTime createdAt;
    private long upvotes;
    private long downvotes;

    public PostResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public PostCategory getCategory() { return category; }
    public void setCategory(PostCategory category) { this.category = category; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public long getUpvotes() { return upvotes; }
    public void setUpvotes(long upvotes) { this.upvotes = upvotes; }

    public long getDownvotes() { return downvotes; }
    public void setDownvotes(long downvotes) { this.downvotes = downvotes; }
}
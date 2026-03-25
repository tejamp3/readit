package com.readit.dto.request;

import com.readit.entity.Post.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostRequest {

    @NotBlank
    @Size(min = 5, max = 200)
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private PostCategory category;

    public PostRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public PostCategory getCategory() { return category; }
    public void setCategory(PostCategory category) { this.category = category; }
}
package com.readit.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {

    @NotBlank
    private String content;

    // null = top-level comment, non-null = reply
    private Long parentId;

    public CommentRequest() {}

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}
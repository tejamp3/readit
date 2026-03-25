package com.readit.dto.response;

import java.time.LocalDateTime;

public class NoteResponse {

    private Long id;
    private String title;
    private String description;
    private String subject;
    private String fileName;
    private String uploaderUsername;
    private LocalDateTime createdAt;
    private long upvotes;
    private long downvotes;

    public NoteResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getUploaderUsername() { return uploaderUsername; }
    public void setUploaderUsername(String uploaderUsername) { this.uploaderUsername = uploaderUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public long getUpvotes() { return upvotes; }
    public void setUpvotes(long upvotes) { this.upvotes = upvotes; }

    public long getDownvotes() { return downvotes; }
    public void setDownvotes(long downvotes) { this.downvotes = downvotes; }
}
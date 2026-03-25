package com.readit.controller;

import com.readit.dto.request.CommentRequest;
import com.readit.dto.response.CommentResponse;
import com.readit.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // POST /api/posts/{postId}/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> addCommentToPost(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponse response = commentService.addCommentToPost(
                postId, request, userDetails.getUsername());
        return ResponseEntity.status(201).body(response);
    }

    // GET /api/posts/{postId}/comments
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId));
    }

    // POST /api/notes/{noteId}/comments
    @PostMapping("/notes/{noteId}/comments")
    public ResponseEntity<CommentResponse> addCommentToNote(
            @PathVariable Long noteId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponse response = commentService.addCommentToNote(
                noteId, request, userDetails.getUsername());
        return ResponseEntity.status(201).body(response);
    }

    // GET /api/notes/{noteId}/comments
    @GetMapping("/notes/{noteId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForNote(@PathVariable Long noteId) {
        return ResponseEntity.ok(commentService.getCommentsForNote(noteId));
    }

    // DELETE /api/comments/{commentId}
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
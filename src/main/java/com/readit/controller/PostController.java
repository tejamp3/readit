package com.readit.controller;

import com.readit.dto.request.PostRequest;
import com.readit.dto.request.VoteRequest;
import com.readit.dto.response.PostResponse;
import com.readit.entity.Post.PostCategory;
import com.readit.service.PostService;
import com.readit.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final VoteService voteService;

    public PostController(PostService postService, VoteService voteService) {
        this.postService = postService;
        this.voteService = voteService;
    }

    // POST /api/posts
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        PostResponse response = postService.createPost(request, userDetails.getUsername());
        return ResponseEntity.status(201).body(response);
    }

    // GET /api/posts?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.getFeed(pageable));
    }

    // GET /api/posts?category=INTERVIEW_EXPERIENCE&page=0&size=10
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<PostResponse>> getFeedByCategory(
            @PathVariable PostCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.getFeedByCategory(category, pageable));
    }

    // GET /api/posts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // PUT /api/posts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        PostResponse response = postService.updatePost(id, request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    // DELETE /api/posts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // POST /api/posts/{id}/vote
    @PostMapping("/{id}/vote")
    public ResponseEntity<String> voteOnPost(
            @PathVariable Long id,
            @Valid @RequestBody VoteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String result = voteService.voteOnPost(id, request, userDetails.getUsername());
        return ResponseEntity.ok(result);
    }
}
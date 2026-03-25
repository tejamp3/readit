package com.readit.service;

import com.readit.dto.request.PostRequest;
import com.readit.dto.response.PostResponse;
import com.readit.entity.Post;
import com.readit.entity.Post.PostCategory;
import com.readit.entity.User;
import com.readit.entity.Vote.VoteType;
import com.readit.exception.ResourceNotFoundException;
import com.readit.exception.UnauthorizedException;
import com.readit.repository.PostRepository;
import com.readit.repository.UserRepository;
import com.readit.repository.VoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public PostResponse createPost(PostRequest request, String email) {
        User user = getUserByEmail(email);
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setAuthor(user);
        postRepository.save(post);
        return mapToResponse(post);
    }

    public Page<PostResponse> getFeed(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapToResponse);
    }

    public Page<PostResponse> getFeedByCategory(PostCategory category, Pageable pageable) {
        return postRepository.findByCategoryOrderByCreatedAtDesc(category, pageable)
                .map(this::mapToResponse);
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return mapToResponse(post);
    }

    public PostResponse updatePost(Long id, PostRequest request, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getEmail().equals(email)) {
            throw new UnauthorizedException("You can only edit your own posts");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        postRepository.save(post);
        return mapToResponse(post);
    }

    public void deletePost(Long id, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getEmail().equals(email)) {
            throw new UnauthorizedException("You can only delete your own posts");
        }

        postRepository.delete(post);
    }

    private PostResponse mapToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setCategory(post.getCategory());
        response.setAuthorUsername(post.getAuthor().getUsername());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpvotes(voteRepository.countByPostIdAndVoteType(post.getId(), VoteType.UPVOTE));
        response.setDownvotes(voteRepository.countByPostIdAndVoteType(post.getId(), VoteType.DOWNVOTE));
        return response;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
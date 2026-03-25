package com.readit.service;

import com.readit.dto.request.CommentRequest;
import com.readit.dto.response.CommentResponse;
import com.readit.entity.Comment;
import com.readit.entity.Note;
import com.readit.entity.Post;
import com.readit.entity.User;
import com.readit.exception.ResourceNotFoundException;
import com.readit.exception.UnauthorizedException;
import com.readit.repository.CommentRepository;
import com.readit.repository.NoteRepository;
import com.readit.repository.PostRepository;
import com.readit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          NoteRepository noteRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public CommentResponse addCommentToPost(Long postId, CommentRequest request, String email) {
        User user = getUserByEmail(email);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(user);
        comment.setPost(post);

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            comment.setParent(parent);
        }

        commentRepository.save(comment);
        return mapToResponse(comment);
    }

    public CommentResponse addCommentToNote(Long noteId, CommentRequest request, String email) {
        User user = getUserByEmail(email);
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(user);
        comment.setNote(note);

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            comment.setParent(parent);
        }

        commentRepository.save(comment);
        return mapToResponse(comment);
    }

    public List<CommentResponse> getCommentsForPost(Long postId) {
        List<Comment> rootComments = commentRepository
                .findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);
        return rootComments.stream()
                .map(this::mapToResponseWithReplies)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getCommentsForNote(Long noteId) {
        List<Comment> rootComments = commentRepository
                .findByNoteIdAndParentIsNullOrderByCreatedAtAsc(noteId);
        return rootComments.stream()
                .map(this::mapToResponseWithReplies)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new UnauthorizedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    // Recursively maps replies — fine for typical nesting depth (2-3 levels)
    private CommentResponse mapToResponseWithReplies(Comment comment) {
        CommentResponse response = mapToResponse(comment);
        List<Comment> replies = commentRepository
                .findByParentIdOrderByCreatedAtAsc(comment.getId());
        response.setReplies(replies.stream()
                .map(this::mapToResponseWithReplies)
                .collect(Collectors.toList()));
        return response;
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setAuthorUsername(comment.getAuthor().getUsername());
        response.setCreatedAt(comment.getCreatedAt());
        response.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        return response;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
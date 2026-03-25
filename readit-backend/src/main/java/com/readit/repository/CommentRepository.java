package com.readit.repository;

import com.readit.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Top-level comments on a post (parent is null)
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtAsc(Long postId);

    // Top-level comments on a note (parent is null)
    List<Comment> findByNoteIdAndParentIsNullOrderByCreatedAtAsc(Long noteId);

    // Replies to a specific comment
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
}
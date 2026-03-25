package com.readit.repository;

import com.readit.entity.Post;
import com.readit.entity.Post.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Home feed — sorted by createdAt desc (passed via Pageable)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Filter by category
    Page<Post> findByCategoryOrderByCreatedAtDesc(PostCategory category, Pageable pageable);

    // All posts by a specific user
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long userId);
}
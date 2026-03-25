package com.readit.repository;

import com.readit.entity.Vote;
import com.readit.entity.Vote.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    // Check if user already voted on a post
    Optional<Vote> findByUserIdAndPostId(Long userId, Long postId);

    // Check if user already voted on a note
    Optional<Vote> findByUserIdAndNoteId(Long userId, Long noteId);

    // Count upvotes/downvotes on a post
    long countByPostIdAndVoteType(Long postId, VoteType voteType);

    // Count upvotes/downvotes on a note
    long countByNoteIdAndVoteType(Long noteId, VoteType voteType);
}
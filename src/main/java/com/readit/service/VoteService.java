package com.readit.service;

import com.readit.dto.request.VoteRequest;
import com.readit.entity.Note;
import com.readit.entity.Post;
import com.readit.entity.User;
import com.readit.entity.Vote;
import com.readit.exception.ResourceNotFoundException;
import com.readit.repository.NoteRepository;
import com.readit.repository.PostRepository;
import com.readit.repository.UserRepository;
import com.readit.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository,
                       PostRepository postRepository,
                       NoteRepository noteRepository,
                       UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public String voteOnPost(Long postId, VoteRequest request, String email) {
        User user = getUserByEmail(email);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Optional<Vote> existing = voteRepository.findByUserIdAndPostId(user.getId(), postId);

        if (existing.isPresent()) {
            Vote vote = existing.get();
            if (vote.getVoteType() == request.getVoteType()) {
                // Same vote again → remove it (toggle off)
                voteRepository.delete(vote);
                return "Vote removed";
            } else {
                // Flip vote
                vote.setVoteType(request.getVoteType());
                voteRepository.save(vote);
                return "Vote updated";
            }
        }

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPost(post);
        vote.setVoteType(request.getVoteType());
        voteRepository.save(vote);
        return "Vote recorded";
    }

    public String voteOnNote(Long noteId, VoteRequest request, String email) {
        User user = getUserByEmail(email);
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        Optional<Vote> existing = voteRepository.findByUserIdAndNoteId(user.getId(), noteId);

        if (existing.isPresent()) {
            Vote vote = existing.get();
            if (vote.getVoteType() == request.getVoteType()) {
                voteRepository.delete(vote);
                return "Vote removed";
            } else {
                vote.setVoteType(request.getVoteType());
                voteRepository.save(vote);
                return "Vote updated";
            }
        }

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setNote(note);
        vote.setVoteType(request.getVoteType());
        voteRepository.save(vote);
        return "Vote recorded";
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
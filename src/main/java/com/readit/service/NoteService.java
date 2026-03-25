package com.readit.service;

import com.readit.dto.request.NoteRequest;
import com.readit.dto.response.NoteResponse;
import com.readit.entity.Note;
import com.readit.entity.User;
import com.readit.entity.Vote.VoteType;
import com.readit.exception.ResourceNotFoundException;
import com.readit.exception.UnauthorizedException;
import com.readit.repository.NoteRepository;
import com.readit.repository.UserRepository;
import com.readit.repository.VoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    private final String uploadDir = "uploads/notes/";

    public NoteService(NoteRepository noteRepository,
                       UserRepository userRepository,
                       VoteRepository voteRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public NoteResponse uploadNote(NoteRequest request, MultipartFile file, String email)
            throws IOException {

        User user = getUserByEmail(email);

        // Store file to local disk
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) Files.createDirectories(dirPath);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = dirPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setDescription(request.getDescription());
        note.setSubject(request.getSubject());
        note.setFileName(fileName);
        note.setFilePath(filePath.toString());
        note.setUploader(user);

        noteRepository.save(note);
        return mapToResponse(note);
    }

    public Page<NoteResponse> getAllNotes(Pageable pageable) {
        return noteRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapToResponse);
    }

    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        return mapToResponse(note);
    }

    public void deleteNote(Long id, String email) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        if (!note.getUploader().getEmail().equals(email)) {
            throw new UnauthorizedException("You can only delete your own notes");
        }

        // Delete file from disk
        try {
            Files.deleteIfExists(Paths.get(note.getFilePath()));
        } catch (IOException e) {
            // Log but don't block deletion
        }

        noteRepository.delete(note);
    }

    private NoteResponse mapToResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setDescription(note.getDescription());
        response.setSubject(note.getSubject());
        response.setFileName(note.getFileName());
        response.setUploaderUsername(note.getUploader().getUsername());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpvotes(voteRepository.countByNoteIdAndVoteType(note.getId(), VoteType.UPVOTE));
        response.setDownvotes(voteRepository.countByNoteIdAndVoteType(note.getId(), VoteType.DOWNVOTE));
        return response;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
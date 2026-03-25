package com.readit.controller;

import com.readit.dto.request.NoteRequest;
import com.readit.dto.request.VoteRequest;
import com.readit.dto.response.NoteResponse;
import com.readit.service.NoteService;
import com.readit.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final VoteService voteService;

    public NoteController(NoteService noteService, VoteService voteService) {
        this.noteService = noteService;
        this.voteService = voteService;
    }

    // POST /api/notes  (multipart/form-data)
    // Form fields: title, description, subject + file part: "file"
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NoteResponse> uploadNote(
            @RequestPart("title") String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("subject") String subject,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        NoteRequest request = new NoteRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setSubject(subject);

        NoteResponse response = noteService.uploadNote(request, file, userDetails.getUsername());
        return ResponseEntity.status(201).body(response);
    }

    // GET /api/notes?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<NoteResponse>> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(noteService.getAllNotes(pageable));
    }

    // GET /api/notes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    // DELETE /api/notes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        noteService.deleteNote(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // POST /api/notes/{id}/vote
    @PostMapping("/{id}/vote")
    public ResponseEntity<String> voteOnNote(
            @PathVariable Long id,
            @Valid @RequestBody VoteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String result = voteService.voteOnNote(id, request, userDetails.getUsername());
        return ResponseEntity.ok(result);
    }
}
package com.readit.repository;

import com.readit.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // All notes — paginated
    Page<Note> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Filter by subject
    List<Note> findBySubjectIgnoreCaseOrderByCreatedAtDesc(String subject);

    // All notes uploaded by a user
    List<Note> findByUploaderIdOrderByCreatedAtDesc(Long userId);
}
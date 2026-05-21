package com.example.lab1.repository;

import com.example.lab1.model.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for NoteRepository.
 */
@DataJpaTest
class NoteRepositoryTest {

    /**
     * Autowired instance of NoteRepository.
     */
    @Autowired
    private NoteRepository noteRepository;

    /**
     * Test saving and retrieving notes.
     */
    @Test
    void shouldSaveAndFindAllNotes() {
        Note note = new Note();

        note.setTitle("Repository Test");
        note.setContent("Testing JpaRepository behavior");

        noteRepository.save(note);
        List<Note> notes = noteRepository.findAll();

        assertThat(notes).isNotEmpty();
        assertThat(notes.size()).isEqualTo(1);
    }
}

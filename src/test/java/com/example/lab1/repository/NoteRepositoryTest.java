package com.example.lab1.repository;

import com.example.lab1.model.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void shouldSaveAndFindNoteById() {
        // Given
        Note note = new Note();
        note.setTitle("Lab 3");
        note.setContent("Cover code with tests");

        // When
        Note savedNote = noteRepository.save(note);
        Optional<Note> foundNote = noteRepository.findById(savedNote.getId());

        // Then
        assertThat(foundNote).isPresent();
        assertThat(foundNote.get().getTitle()).isEqualTo("Lab 3");
        assertThat(foundNote.get().getContent()).isEqualTo("Cover code with tests");
    }
}
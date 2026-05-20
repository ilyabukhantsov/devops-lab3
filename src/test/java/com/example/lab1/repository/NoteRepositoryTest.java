package com.example.lab1.repository;

import com.example.lab1.model.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void shouldSaveAndFindNotes() {
        Note note = new Note();
        note.setTitle("Lab 3");
        note.setContent("Testing");

        noteRepository.save(note);
        List<Note> notes = noteRepository.findAll();

        assertThat(notes).isNotEmpty();
        assertThat(notes.get(0).getTitle()).isEqualTo("Lab 3");
    }
}
package com.example.lab1.service;

import com.example.lab1.model.Note;
import com.example.lab1.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void shouldCreateNoteSuccessfully() {
        // Given
        Note note = new Note();
        note.setTitle("Service Test");
        note.setContent("Testing service layer");

        when(noteRepository.save(any(Note.class))).thenReturn(note);

        // When
        Note created = noteService.createNote(note);

        // Then
        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Service Test");
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void shouldReturnNoteWhenIdExists() {
        // Given
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Existing Note");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        // When
        Optional<Note> found = noteService.getNoteById(1L);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Existing Note");
    }
}
package com.example.lab1.service;

import com.example.lab1.model.Note;
import com.example.lab1.repository.NoteRepository;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void testFindAll() {
        when(noteRepository.findAll()).thenReturn(Collections.emptyList());
        noteService.findAll();
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        Note note = new Note();
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        noteService.save(note);
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testFindById() {
        noteService.findById(1L);
        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        noteService.deleteById(1L);
        verify(noteRepository, times(1)).deleteById(1L);
    }
}
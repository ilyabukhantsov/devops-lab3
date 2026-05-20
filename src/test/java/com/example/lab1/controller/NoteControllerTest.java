package com.example.lab1.controller;

import com.example.lab1.model.Note;
import com.example.lab1.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnNoteById() throws Exception {
        // Given
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Controller Test");
        note.setContent("MockMvc is awesome");

        when(noteService.getNoteById(1L)).thenReturn(Optional.of(note));

        // When & Then
        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Controller Test"))
                .andExpect(jsonPath("$.content").value("MockMvc is awesome"));
    }

    @Test
    void shouldCreateNoteAndReturnCreated() throws Exception {
        // Given
        Note note = new Note();
        note.setTitle("New Note");
        note.setContent("Content");

        when(noteService.createNote(any(Note.class))).thenReturn(note);

        // When & Then
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Note"));
    }
}
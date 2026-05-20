package com.example.lab1.controller;

import com.example.lab1.model.Note;
import com.example.lab1.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({NoteController.class, HealthController.class})
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private DataSource dataSource;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllNotes() throws Exception {
        when(noteService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/notes")
                        .header("Accept", "application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNote() throws Exception {
        Note note = new Note();
        // Если у тебя в Note другие сеттеры (например, text вместо content), поправь их тут
        note.setTitle("CI/CD");
        note.setContent("Lab 3 tests");

        when(noteService.save(any(Note.class))).thenReturn(note);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNoteById() throws Exception {
        Note note = new Note();
        note.setTitle("Found");

        when(noteService.findById(1L)).thenReturn(note);

        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenNoteDoesNotExist() throws Exception {
        when(noteService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/notes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteNote() throws Exception {
        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnAliveStatus() throws Exception {
        mockMvc.perform(get("/health/alive"))
                .andExpect(status().isOk())
                .andExpect(content().string("Alive"));
    }

    @Test
    void shouldReturnReadyStatusWhenDbIsValid() throws Exception {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isValid(1)).thenReturn(true);
        when(dataSource.getConnection()).thenReturn(mockConnection);

        mockMvc.perform(get("/health/ready"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ready"));
    }
}
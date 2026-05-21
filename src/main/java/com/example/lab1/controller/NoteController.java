package com.example.lab1.controller;

import com.example.lab1.model.Note;
import com.example.lab1.service.NoteService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing notes.
 */
@RestController
@RequestMapping("/api/notes")
public final class NoteController {

    /**
     * Service instance for note operations.
     */
    private final NoteService noteService;

    /**
     * Constructs a new NoteController.
     * @param service the note service dependency
     */
    public NoteController(final NoteService service) {
        this.noteService = service;
    }

    /**
     * Retrieves all notes.
     * @param accept the accept header value
     * @return response entity with list of notes
     */
    @GetMapping
    public ResponseEntity<List<Note>> getAll(
            @RequestHeader(value = "Accept",
                    defaultValue = "application/json")
            final String accept) {
        List<Note> notes = noteService.findAll();
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    /**
     * Creates a new note.
     * @param note the note to create
     * @return response entity with created note
     */
    @PostMapping
    public ResponseEntity<Note> create(
            @RequestBody final Note note) {
        Note created = noteService.save(note);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves a note by its identifier.
     * @param id the identifier of the note
     * @param accept the accept header value
     * @return response entity with the found note
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(
            @PathVariable final Long id,
            @RequestHeader(value = "Accept",
                    defaultValue = "application/json")
            final String accept) {
        Note note = noteService.findById(id);
        if (note == null) {
            String msg = "Note with id "
                    + id
                    + " not found. "
                    + "Please check the "
                    + "provided identifier "
                    + "and try again.";
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    /**
     * Deletes a note by its identifier.
     * @param id the identifier of the note to delete
     * @return response entity with no content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable final Long id) {
        noteService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


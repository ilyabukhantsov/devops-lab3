package com.example.lab1.service;

import com.example.lab1.model.Note;
import java.util.List;

/**
 * Service interface for managing notes.
 */
public interface NoteService {

    /**
     * Retrieves all existing notes.
     * @return a list of all notes
     */
    List<Note> findAll();

    /**
     * Finds a specific note by its identifier.
     * @param id the identifier of the note
     * @return the found note, or null if it does not exist
     */
    Note findById(Long id);

    /**
     * Saves or updates a note.
     * @param note the note entity to save
     * @return the persisted note entity
     */
    Note save(Note note);

    /**
     * Deletes a note by its identifier.
     * @param id the identifier of the note to delete
     */
    void deleteById(Long id);
}


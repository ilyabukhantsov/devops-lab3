package com.example.lab1.service;

import com.example.lab1.model.Note;
import com.example.lab1.repository.NoteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Production implementation of the NoteService.
 */
@Service
public final class NoteServiceImpl implements NoteService {

    /**
     * Repository dependency for note data access.
     */
    private final NoteRepository repository;

    /**
     * Constructs a new NoteServiceImpl instance.
     * @param noteRepository the note repository dependency
     */
    public NoteServiceImpl(final NoteRepository noteRepository) {
        this.repository = noteRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note findById(final Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note save(final Note note) {
        return repository.save(note);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}


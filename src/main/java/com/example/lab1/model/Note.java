package com.example.lab1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

/**
 * Entity class representing a Note.
 */
@Entity
public final class Note {

    /** Unique identifier of the note. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Title of the note. */
    private String title;

    /** Text content of the note. */
    private String content;

    /** Timestamp when the note was created. */
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Default constructor required by JPA.
     */
    public Note() {
    }

    /**
     * Custom constructor to create a note with title and content.
     * @param pTitle the title of the note
     * @param pContent the content of the note
     */
    public Note(final String pTitle, final String pContent) {
        this.title = pTitle;
        this.content = pContent;
    }

    /**
     * Gets the unique identifier.
     * @return the id of the note
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the title.
     * @return the title of the note
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     * @param pTitle the new title to set
     */
    public void setTitle(final String pTitle) {
        this.title = pTitle;
    }

    /**
     * Gets the content.
     * @return the content of the note
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content.
     * @param pContent the new content to set
     */
    public void setContent(final String pContent) {
        this.content = pContent;
    }

    /**
     * Gets the creation timestamp.
     * @return the date and time the note was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

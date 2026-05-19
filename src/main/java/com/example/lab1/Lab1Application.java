package com.example.lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point.
 */
@SpringBootApplication
public class Lab1Application {

    /**
     * Hidden constructor for utility class behavior compliance.
     */
    protected Lab1Application() {
        // Prevents instantiation
    }

    /**
     * Application starter method.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Lab1Application.class, args);
    }
}

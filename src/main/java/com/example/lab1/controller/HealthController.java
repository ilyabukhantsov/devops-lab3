package com.example.lab1.controller;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health check controller for monitoring application state.
 */
@RestController
public final class HealthController {

    /**
     * Data source instance for database health checks.
     */
    private final DataSource dataSource;

    /**
     * Constructs a new HealthController.
     * @param dbDataSource the database data source
     */
    public HealthController(final DataSource dbDataSource) {
        this.dataSource = dbDataSource;
    }

    /**
     * Liveness probe endpoint.
     * @return response entity indicating application is alive
     */
    @GetMapping("/health/alive")
    public ResponseEntity<String> alive() {
        return new ResponseEntity<>("Alive", HttpStatus.OK);
    }

    /**
     * Readiness probe endpoint verifying database connectivity.
     * @return response entity indicating application readiness
     */
    @GetMapping("/health/ready")
    public ResponseEntity<String> ready() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                return new ResponseEntity<>("Ready", HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    "Database connection invalid",
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        } catch (SQLException e) {
            return new ResponseEntity<>(
                    "Database unavailable: " + e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }
}



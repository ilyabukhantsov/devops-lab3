package com.example.lab1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Application context loading test.
 */
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password="
})
class Lab1ApplicationTests {

    /**
     * Verifies that context boots up successfully.
     */
    @Test
    void contextLoads() {
        // Confirms successful context initialization
    }
}
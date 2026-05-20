package com.example.lab1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import javax.sql.DataSource;

@SpringBootTest
class Lab1ApplicationTests {

    @MockBean
    private DataSource dataSource;

    @Test
    void contextLoads() {
    }
}
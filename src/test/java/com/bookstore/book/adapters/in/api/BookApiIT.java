package com.bookstore.book.adapters.in.api;

import com.bookstore.book.BookApplication;
import com.bookstore.book.adapters.in.api.request.BookTypeCreateRequest;
import com.bookstore.book.adapters.in.api.request.CreateBookRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BookApplication.class
)
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
@Testcontainers
public class BookApiIT {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Container
    protected static DockerComposeContainer<?> dockerComposeContainer = new DockerComposeContainer<>(new File("./docker-compose-integration-test.yml"));

    @Test
    void applicationContextLoads() {}

    @BeforeAll
    static void setupDockerCompose() {
        dockerComposeContainer.start();
    }

    @AfterAll
    static void tearDown() {
        dockerComposeContainer.stop();
    }

    @DisplayName("Should Create Book - Integration Test")
    @Test
    void should_createBook() throws Exception {

        CreateBookRequest createBookRequest = CreateBookRequest.builder()
                .author("Vinicius")
                .base_price(9.99f)
                .description("Book about Spring Boot")
                .quantity(10)
                .title("Spring Boot")
                .type(BookTypeCreateRequest.NEW_RELEASE)
                .build();

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/book/v1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createBookRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("Vinicius")));
    }
}

package com.careerhub.shared.error;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldMapConflictExceptionToConflictProblem() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value("Test conflict."));
    }

    @Test
    void shouldMapNotFoundExceptionToNotFoundProblem() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Test resource not found."));
    }

    @Test
    void shouldMapDataIntegrityViolationWithoutLeakingDatabaseDetails() throws Exception {
        mockMvc.perform(get("/test/integrity"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail")
                        .value("The request conflicts with existing data."))
                .andExpect(content().string(not(containsString("user_email_key"))));
    }

    @Test
    void shouldMapUnexpectedExceptionWithoutLeakingItsMessage() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred."))
                .andExpect(content().string(not(containsString("internal secret"))));
    }

    @Test
    void shouldReportFieldErrorsWhenValidationFails() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Request validation failed."))
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @RestController
    static class TestController {

        @GetMapping("/test/conflict")
        void conflict() {
            throw new TestConflictException();
        }

        @GetMapping("/test/not-found")
        void notFound() {
            throw new TestNotFoundException();
        }

        @GetMapping("/test/integrity")
        void integrity() {
            throw new DataIntegrityViolationException(
                    "duplicate key value violates unique constraint \"user_email_key\"");
        }

        @GetMapping("/test/unexpected")
        void unexpected() {
            throw new IllegalStateException("internal secret");
        }

        @PostMapping("/test/validation")
        void validation(@Valid @RequestBody TestRequest request) {
        }
    }

    static class TestConflictException extends ConflictException {

        TestConflictException() {
            super("Test conflict.");
        }
    }

    static class TestNotFoundException extends NotFoundException {

        TestNotFoundException() {
            super("Test resource not found.");
        }
    }

    record TestRequest(@NotBlank String name) {
    }
}
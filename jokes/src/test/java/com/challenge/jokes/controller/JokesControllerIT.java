package com.challenge.jokes.controller;

import com.challenge.jokes.dto.response.ResponseDTO;
import com.challenge.jokes.dto.response.ValidationErrorResponseDTO;
import com.challenge.jokes.repository.JokesRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test for the rest endpoint retrieving jokes.
 */
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@Slf4j
class JokesControllerIT {

    private final static String SUCCESS_MSG_TEMPLATE = "Jokes retrieved for the requested number: %d";
    private final static String ERR_MSG = "Count validation error";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JokesRepository jokesRepository;

    /**
     * Test successful response for the rest endpoint
     *
     * @throws Exception the exception
     */
    @Test
    void shouldFetchJokes() throws Exception {

        // Given
        int numberToFetch = 3;
        String expectedMessage = String.format(SUCCESS_MSG_TEMPLATE, numberToFetch);

        // When
        MvcResult result = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/jokes").param("count", String.valueOf(numberToFetch)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ResponseDTO responseDTO = new Gson().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        log.info("Response DTO: {}", responseDTO);

        assertEquals(expectedMessage, responseDTO.message());
        assertEquals(numberToFetch, responseDTO.jokesList().size(), "Unexpected number of retrieved jokes");
    }

    /**
     * Test error response when the requested number of jokes is too large
     *
     * @throws Exception the exception
     */
    @Test
    void shouldReturnErrorMsgForLargeFetchNumber() throws Exception {

        // Given
        int largeNumberToFetch = 1000;

        // When
        MvcResult result = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/jokes").param("count", String.valueOf(largeNumberToFetch)))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        // Then
        ValidationErrorResponseDTO errorResponseDTO =
                new Gson().fromJson(result.getResponse().getContentAsString(), ValidationErrorResponseDTO.class);
        log.info("Error response DTO: {}", errorResponseDTO);
        assertEquals(ERR_MSG, errorResponseDTO.message());
    }
}
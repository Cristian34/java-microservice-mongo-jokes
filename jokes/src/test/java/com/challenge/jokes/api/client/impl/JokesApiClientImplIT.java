package com.challenge.jokes.api.client.impl;

import com.challenge.jokes.dto.JokeDTO;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for {@link com.challenge.jokes.service.impl.JokesServiceImpl}
 */
@Slf4j
class JokesApiClientImplIT {

    private static final String API_ENDPOINT = "https://official-joke-api.appspot.com/random_joke";

    private final JokesApiClientImpl jokesApiClient = new JokesApiClientImpl(RestClient.create(API_ENDPOINT));

    /**
     * Verify the joke returned from the external source
     */
    @Test
    void shouldFetchJoke() {

        // When
        JokeDTO jokeDTO = jokesApiClient.fetchJoke();

        // Then
        assertNotNull(jokeDTO);
        assertNotNull(jokeDTO.id());
        assertTrue(StringUtils.isNotBlank(jokeDTO.type()));
        assertTrue(StringUtils.isNotBlank(jokeDTO.setup()));
        assertTrue(StringUtils.isNotBlank(jokeDTO.punchline()));
        log.info("Retrieved joke: {}", jokeDTO);
    }
}
package com.challenge.jokes.api.client.impl;

import com.challenge.jokes.api.client.IJokesApiClient;
import com.challenge.jokes.dto.JokeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Implementation issuing requests to the "Official joke api"
 */
@Service
@Slf4j
public class JokesApiClientImpl implements IJokesApiClient {

    @Value("${jokes-api.request.endpoint}")
    private String apiEndpoint;

    /**
     * Provide method with default access, to support testing
     *
     * @param apiEndpoint the API endpoint to set
     */
    void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JokeDTO fetchJoke() {
        log.info("fetchJoke(..) - Start fetching joke from api {}", apiEndpoint);
        RestClient restClient = RestClient.create();
        JokeDTO joke = restClient.get()
                .uri(apiEndpoint)
                .retrieve()
                .body(JokeDTO.class);
        log.info("fetchJoke(..) - End fetching joke {}", joke);
        return joke;
    }

}

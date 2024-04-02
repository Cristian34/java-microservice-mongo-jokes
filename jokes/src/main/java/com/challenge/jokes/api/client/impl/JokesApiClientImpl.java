package com.challenge.jokes.api.client.impl;

import com.challenge.jokes.api.client.IJokesApiClient;
import com.challenge.jokes.dto.JokeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Implementation issuing requests to the "Official joke api"
 */
@Service
@AllArgsConstructor
@Slf4j
public class JokesApiClientImpl implements IJokesApiClient {

    private RestClient restClient;

    @Override
    public JokeDTO fetchJoke() {
        log.info("fetchJoke(..) - Start fetching joke");
        JokeDTO joke = restClient.get()
                .retrieve()
                .body(JokeDTO.class);
        log.info("fetchJoke(..) - End fetching joke {}", joke);
        return joke;
    }

}

package com.challenge.jokes.api.client;

import com.challenge.jokes.dto.JokeDTO;

/**
 * Client launching requests to the source providing jokes
 */
public interface IJokesApiClient {

    /**
     * Retrieve a joke from the jokes external source
     *
     * @return the retrieved joke
     */
    JokeDTO fetchJoke();

}

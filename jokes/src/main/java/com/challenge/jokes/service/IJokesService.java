package com.challenge.jokes.service;

import com.challenge.jokes.dto.JokeDTO;

import java.util.List;

/**
 * Service for fetching and storing jokes
 */
public interface IJokesService {

    /**
     * Fetch jokes from external API
     *
     * @param count the number of jokes to be fetched
     * @return the list of fetched jokes
     */
    List<JokeDTO> fetchJokesExternalApi(int count);

    /**
     * Stores the provided list of jokes
     *
     * @param jokesDTOList
     *          the list of jokes to be saved
     */
    void saveJokes(List<JokeDTO> jokesDTOList);

}

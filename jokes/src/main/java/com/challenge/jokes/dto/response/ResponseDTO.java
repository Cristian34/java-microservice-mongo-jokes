package com.challenge.jokes.dto.response;

import com.challenge.jokes.dto.JokeDTO;

import java.util.List;

/**
 * DTO storing the response message in case of success
 */
public record ResponseDTO(String message, List<JokeDTO> jokesList) {}

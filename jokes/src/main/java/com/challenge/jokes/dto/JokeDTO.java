package com.challenge.jokes.dto;

/**
 * DTO storing the joke content
 */
public record JokeDTO(Long id, String type, String setup, String punchline) {}

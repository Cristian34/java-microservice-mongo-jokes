package com.challenge.jokes.dto.response;

import java.time.LocalDateTime;

/**
 * DTO storing the response message in case of error
 */
public record ErrorResponseDTO(String errorMessage, LocalDateTime errorTime) {}

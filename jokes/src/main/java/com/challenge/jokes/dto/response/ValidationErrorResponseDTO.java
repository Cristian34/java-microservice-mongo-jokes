package com.challenge.jokes.dto.response;

import java.util.List;

/**
 * DTO storing the response message in case of input data validation error
 */
public record ValidationErrorResponseDTO(String message, List<String> validationErrors) {}

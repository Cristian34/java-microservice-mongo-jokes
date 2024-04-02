package com.challenge.jokes.exception;

/**
 * Custom exception related to the "fetch joke" operation
 */
public class FetchJokeException extends RuntimeException {

    public FetchJokeException(String errorMessage) {
        super(errorMessage);
    }
}

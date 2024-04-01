package com.challenge.jokes.repository;

import com.challenge.jokes.model.Joke;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoDB repository for storing jokes
 */
public interface JokesRepository extends MongoRepository<Joke, String> {
}

package com.challenge.jokes.controller;

import com.challenge.jokes.dto.JokeDTO;
import com.challenge.jokes.dto.response.ResponseDTO;
import com.challenge.jokes.service.IJokesService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest controllers providing endpoint(s) to fetch and manage jokes
 */
@RestController
@RequestMapping(path = "/jokes", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class JokesController {

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 100;
    private static final String DEFAULT_VALUE = "5";
    private static final String MSG_JOKES_RETRIEVED = "Jokes retrieved for the requested number: %d";

    private final IJokesService jokesService;

    /**
     * Fetch the specified number of random jokes
     *
     * @param count the number of jokes to be fetched
     * @return the fetched jokes
     */
    @GetMapping()
    public ResponseEntity<ResponseDTO> fetchJokes(
            @RequestParam(defaultValue = DEFAULT_VALUE)
            @Min(value = MIN_VALUE) @Max(value = MAX_VALUE) int count) {

        final List<JokeDTO> jokesDTOList = jokesService.fetchJokesExternalApi(count);

        jokesService.saveJokes(jokesDTOList);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO(String.format(MSG_JOKES_RETRIEVED, count), jokesDTOList));
    }

}

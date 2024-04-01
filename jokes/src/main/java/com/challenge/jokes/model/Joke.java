package com.challenge.jokes.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Document storing joke content
 */
@Document
@Getter
@Setter
@ToString
public class Joke {

    @Id
    private String id;

    private Long jokeId;
    private String type;
    private String setup;
    private String punchLine;

    /**
     * Instantiates a new joke
     *
     * @param jokeId    the joke id provided by the external source
     * @param type      the joke type
     * @param setup     the joke intro
     * @param punchLine the joke punch line
     */
    public Joke(Long jokeId, String type, String setup, String punchLine) {
        this.jokeId = jokeId;
        this.type = type;
        this.setup = setup;
        this.punchLine = punchLine;
    }

}

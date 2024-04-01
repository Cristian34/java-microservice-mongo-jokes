package com.challenge.jokes.service.impl;

import com.challenge.jokes.api.client.IJokesApiClient;
import com.challenge.jokes.dto.JokeDTO;
import com.challenge.jokes.model.Joke;
import com.challenge.jokes.repository.JokesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for {@link JokesServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
class JokesServiceImplTest {

    @InjectMocks
    private JokesServiceImpl jokesService;

    @Mock
    private JokesRepository jokesRepository;

    @Mock
    private IJokesApiClient jokesServiceClient;

    @Captor
    private ArgumentCaptor<Joke> jokeDTOArgumentCaptor;

    /**
     * Test saving of one joke inside the repository
     */
    @Test
    void shouldSaveJokeInsideRepository() {

        final long jokeId = 1L;
        final String jokeType = "type";
        final String jokeSetup = "setup";
        final String jokePunchline = "punchline";

        // Given
        JokeDTO jokeDTO = new JokeDTO(1L, jokeType, jokeSetup, jokePunchline);

        // When
        jokesService.saveJoke(jokeDTO);

        // Then
        Mockito.verify(jokesRepository, Mockito.times(1)).save(jokeDTOArgumentCaptor.capture());
        Joke savedJoke = jokeDTOArgumentCaptor.getValue();
        assertEquals(savedJoke.getJokeId(), jokeId, "Unexpected joke id");
        assertEquals(savedJoke.getType(), jokeType, "Unexpected joke type");
        assertEquals(savedJoke.getPunchLine(), jokePunchline, "Unexpected joke punchline");
    }

    /**
     * Test scenario when one joke requested
     */
    @Test
    void shouldFetchOneJokeThroughExternalAPI() {

        // Given
        int numberToFetch = 1;

        Mockito.when(jokesServiceClient.fetchJoke()).thenReturn(createJokeDTO(1));

        // When
        List<JokeDTO> retrievedJokeList = jokesService.fetchJokesExternalApi(numberToFetch);

        // Then
        List<JokeDTO> expectedJokesList = List.of(createJokeDTO(1));
        assertEquals(expectedJokesList, retrievedJokeList, "Unexpected list of jokes");
    }

    /**
     * Test scenario when the number of jokes to request is smaller than the batch size.
     * In this case, only one batch of requests will be processed
     */
    @Test
    void shouldFetchMultipleJokesInOneBatchThroughExternalAPI() {

        // Given
        int numberToFetch = 3;
        Mockito.when(jokesServiceClient.fetchJoke())
                .thenReturn(createJokeDTO(1))
                .thenReturn(createJokeDTO(2))
                .thenReturn(createJokeDTO(3));
        jokesService.setRequestBatchSize(10);

        // When
        List<JokeDTO> retrievedJokeList = jokesService.fetchJokesExternalApi(numberToFetch);

        // Then
        Set<JokeDTO> expectedJokesList = new HashSet<>();
        for (int index = 1; index <= numberToFetch; index++) {
            expectedJokesList.add(createJokeDTO(index));
        }
        assertEquals(expectedJokesList, new HashSet<>(retrievedJokeList), "Unexpected list of jokes");
    }

    /**
     * Test scenario when the number of jokes to request is larger than the batch size.
     * In this case, more than one batch of requests will be processed
     */
    @Test
    void shouldFetchMultipleJokesInMultipleBatchesThroughExternalAPI() {

        // Given
        int numberToFetch = 3;
        Mockito.when(jokesServiceClient.fetchJoke())
                .thenReturn(createJokeDTO(1))
                .thenReturn(createJokeDTO(2))
                .thenReturn(createJokeDTO(3));
        jokesService.setRequestBatchSize(2);

        // When
        List<JokeDTO> retrievedJokeList = jokesService.fetchJokesExternalApi(numberToFetch);

        // Then
        Set<JokeDTO> expectedJokesList = new HashSet<>();
        for (int index = 1; index <= numberToFetch; index++) {
            expectedJokesList.add(createJokeDTO(index));
        }
        assertEquals(expectedJokesList, new HashSet<>(retrievedJokeList), "Unexpected list of jokes");
    }

    /**
     * Test scenario when the method fetching jokes receives an invalid input number
     */
    @Test
    void shouldThrowExceptionForInvalidNumberToFetch() {

        // Given
        int invalidNumber = -1;

        // When - Then
        final Exception ex = assertThrows(IllegalArgumentException.class,
                () -> jokesService.fetchJokesExternalApi(invalidNumber));
    }

    private JokeDTO createJokeDTO(int index) {
        final String jokeType = "type" + index;
        final String jokeSetup = "setup" + index;
        final String jokePunchline = "punchline" + index;
        return new JokeDTO((long) index, jokeType, jokeSetup, jokePunchline);
    }
}
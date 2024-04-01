package com.challenge.jokes.service.impl;

import com.challenge.jokes.api.client.IJokesApiClient;
import com.challenge.jokes.dto.JokeDTO;
import com.challenge.jokes.exception.FetchJokeException;
import com.challenge.jokes.model.Joke;
import com.challenge.jokes.repository.JokesRepository;
import com.challenge.jokes.service.IJokesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * {@inheritDoc}
 */
@Service
@Slf4j
public class JokesServiceImpl implements IJokesService {

    @Value("${jokes-api.request.fetch.batch.size:10}")
    private int requestBatchSize;

    private final IJokesApiClient jokesServiceClient;

    private final JokesRepository jokesRepository;

    /**
     * Constructor used to instantiate services.
     *
     * @param jokesServiceClient the services used to retrieve jokes from an external source
     * @param jokesRepository    the repository used to store jokes
     */
    public JokesServiceImpl(IJokesApiClient jokesServiceClient, JokesRepository jokesRepository) {
        this.jokesServiceClient = jokesServiceClient;
        this.jokesRepository = jokesRepository;
    }

    /**
     * Provide method with default access, to support testing
     *
     * @param requestBatchSize the batch size to set
     */
    void setRequestBatchSize(int requestBatchSize) {
        this.requestBatchSize = requestBatchSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveJoke(JokeDTO jokeDTO) {
        Joke joke = new Joke(jokeDTO.id(), jokeDTO.type(), jokeDTO.setup(), jokeDTO.punchline());
        log.info("saveJoke(..) - Save joke: {}", joke);
        jokesRepository.save(joke);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JokeDTO> fetchJokesExternalApi(int count) {

        if (count < 1) {
            throw new IllegalArgumentException("The requested number should be positive");
        }
        try {
            if (count == 1) {
                return fetchJoke();
            } else {
                return fetchJokesInBatches(count);
            }
        } catch (Exception ex) {
            log.error("fetchJokesExternalApi(..) - Error when retrieving jokes", ex);
            throw new FetchJokeException("Error fetching joke from the external source");
        }
    }

    /**
     * Fetch joke synchronously
     *
     * @return the fetched joke
     */
    private List<JokeDTO> fetchJoke() {
        log.info("Retrieve single joke from the joke service");
        return List.of(jokesServiceClient.fetchJoke());
    }

    /**
     * Fetch jokes in batches.
     *
     * @param count the number of jokes to be retrieved
     * @return the retrieved jokes list
     * @throws ExecutionException   exception thrown during the execution of the async operation
     * @throws InterruptedException exception thrown if execution thread is interrupted
     */
    private List<JokeDTO> fetchJokesInBatches(int count) throws ExecutionException, InterruptedException {

        log.info("fetchJokesInBatches(..) - Retrieve {} jokes in batches of {}", count, requestBatchSize);

        final List<JokeDTO> retrievedList = new ArrayList<>();
        int itemsToProcess = count;
        while (itemsToProcess > 0) {
            int chunkSize = itemsToProcess > requestBatchSize ? requestBatchSize : itemsToProcess;
            retrievedList.addAll(fetchBatchOfJokes(chunkSize));
            itemsToProcess = itemsToProcess - chunkSize;
        }

        return retrievedList;
    }

    /**
     * Helper method used to retrieve a batch (chunk) of jokes.
     * The individual requests are executed asynchronously inside the batch.
     * All batch requests should be completed before returning the result.
     *
     * @param chunkSize the processing chunk (batch) size
     * @return the retrieved jokes for the processed batch
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     */
    private List<JokeDTO> fetchBatchOfJokes(int chunkSize) throws InterruptedException, ExecutionException {

        log.info("fetchBatchOfJokes(..) - Retrieve batch of {} jokes", chunkSize);

        // launch parallel requests
        final List<CompletableFuture<JokeDTO>> retrievedListFuture = new ArrayList<>();
        for (int i = 0; i < chunkSize; i++) {
            retrievedListFuture.add(
                    CompletableFuture.supplyAsync(jokesServiceClient::fetchJoke));
        }
        // collect results
        final List<JokeDTO> retrievedList = new ArrayList<>();
        for (CompletableFuture<JokeDTO> jokeFuture : retrievedListFuture) {
            retrievedList.add(jokeFuture.get());
        }
        return retrievedList;
    }
}

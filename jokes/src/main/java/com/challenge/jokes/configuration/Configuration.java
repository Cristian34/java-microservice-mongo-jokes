package com.challenge.jokes.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

/**
 * Generic configuration class
 */
@org.springframework.context.annotation.Configuration
@Slf4j
public class Configuration {

    @Value("${jokes-api.request.endpoint}")
    private String baseURI;

    @Bean
    RestClient restClient() {
        log.info("restClient(..) - Create rest client for URI: {}", baseURI);
        return RestClient.create(baseURI);
    }
}

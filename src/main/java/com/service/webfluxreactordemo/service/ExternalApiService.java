package com.service.webfluxreactordemo.service;

import com.service.webfluxreactordemo.domain.ExternalData;
import com.service.webfluxreactordemo.exception.NotFoundException;
import com.service.webfluxreactordemo.exception.ServiceExecption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
//Making External API Calls with WebClient
//WebClient is the reactive replacement for RestTemplate. It's non-blocking and integrates seamlessly with WebFlux:
public class ExternalApiService {

    private final WebClient webClient;

    // Configure WebClient with base URL and default Headers
    public ExternalApiService(WebClient.Builder webClientBuilder) {
        this.webClient= webClientBuilder
                .baseUrl("https://api.external-service.com")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // Get request returning a single item
    // The retrieve() methods handles response extraction

    public Mono<ExternalData> fetchData(String id){
        return webClient.get()
                .uri("/data/{id}", id)
                .retrieve()
                .bodyToMono(ExternalData.class);
    }

    // Get request returning multiple items as a stream
    // Flux allows processing items as they arrive

    public Flux<ExternalData> findAllData(){
        return  webClient
                .get()
                .uri("/data")
                .retrieve()
                .bodyToFlux(ExternalData.class);
    }


    //POST request with a body
    // bodyValue() serialize the Object to JSON
    public Mono<ExternalData> createData(ExternalData externalData){
        return  webClient
                .post()
                .uri("/data")
                .bodyValue(externalData)
                .retrieve()
                .bodyToMono(ExternalData.class);
    }

    // Handle specific HTTP status codes
    // onStatus() lets you customize error handling
    public Mono<ExternalData> fetchWithErrorhandling( String id){
        return webClient
                .get()
                .uri("/data/{id}", id)
                .retrieve()
                .onStatus(status-> status.value()==404, clientResponse -> Mono.error(new NotFoundException("Data not found")))
                .onStatus( HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ServiceExecption("Invalid external API request")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error( new ServiceExecption("External service error")))
                .bodyToMono(ExternalData.class);
    }

}

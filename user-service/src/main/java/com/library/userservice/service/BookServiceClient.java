package com.library.userservice.service;

import com.library.userservice.model.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BookServiceClient {

    private final WebClient webClient;
    
    @Autowired
    public BookServiceClient(WebClient.Builder webClientBuilder, @Value("${book.service.url}") String bookServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(bookServiceUrl).build();
    }
    
    public Mono<BookDTO> getBookById(Long id) {
        return webClient.get()
                .uri("/api/books/{id}", id)
                .retrieve()
                .bodyToMono(BookDTO.class);
    }
    
    public Mono<BookDTO> updateBookAvailability(Long id, boolean available) {
        return webClient.patch()
                .uri("/api/books/{id}/availability?available={available}", id, available)
                .retrieve()
                .bodyToMono(BookDTO.class);
    }
}
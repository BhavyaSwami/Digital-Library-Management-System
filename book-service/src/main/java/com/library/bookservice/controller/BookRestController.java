package com.library.bookservice.controller;

import com.library.bookservice.model.Book;
import com.library.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book REST Controller", description = "REST APIs for book operations")
@CrossOrigin(origins = "*")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve all books in the library")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a book by its ID")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/title")
    @Operation(summary = "Search books by title", description = "Search for books containing the given title")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @GetMapping("/search/author")
    @Operation(summary = "Search books by author", description = "Search for books by the given author")
    public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @PostMapping
    @Operation(summary = "Add a new book", description = "Add a new book to the library")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book's details")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            return ResponseEntity.ok(bookService.updateBook(id, book));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Remove a book from the library")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Check book availability", description = "Check if a book is available for borrowing")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(book.isAvailable()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update book availability", description = "Update a book's availability status")
    public ResponseEntity<Book> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        try {
            return ResponseEntity.ok(bookService.updateBookAvailability(id, available));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/penalty")
    @Operation(summary = "Calculate penalty", description = "Calculate penalty for an overdue book")
    public ResponseEntity<Double> calculatePenalty(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.calculatePenalty(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.library.bookservice.service;

import com.library.bookservice.model.Book;
import com.library.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    
    // Standard loan period in days
    private static final int LOAN_PERIOD_DAYS = 14;
    // Penalty rate per day (in dollars)
    private static final double PENALTY_RATE = 1.0;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }
    
    public Book addBook(Book book) {
        // Generate QR code if not provided
        if (book.getQrCode() == null || book.getQrCode().isEmpty()) {
            book.setQrCode(generateQrCode());
        }
        return bookRepository.save(book);
    }
    
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setPublisher(bookDetails.getPublisher());
        book.setGenre(bookDetails.getGenre());
        book.setDescription(bookDetails.getDescription());
        book.setCoverImage(bookDetails.getCoverImage());
        
        if (bookDetails.getQrCode() != null && !bookDetails.getQrCode().isEmpty()) {
            book.setQrCode(bookDetails.getQrCode());
        }
        
        return bookRepository.save(book);
    }
    
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        bookRepository.delete(book);
    }
    
    public Book updateBookAvailability(Long id, boolean available) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        book.setAvailable(available);
        return bookRepository.save(book);
    }
    
    /**
     * Borrow a book
     * @param bookId The ID of the book to borrow
     * @param userId The ID of the user borrowing the book
     * @return The borrowed book
     */
    public Book borrowBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        
        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available for borrowing");
        }
        
        // Set borrowing details
        book.setAvailable(false);
        book.setBorrowId(userId);
        book.setBorrowDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));
        
        return bookRepository.save(book);
    }
    
    /**
     * Return a book
     * @param bookId The ID of the book to return
     * @return The returned book
     */
    public Book returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        
        if (book.isAvailable()) {
            throw new RuntimeException("Book is not currently borrowed");
        }
        
        // Reset borrowing details
        book.setAvailable(true);
        book.setBorrowId(null);
        book.setBorrowDate(null);
        book.setDueDate(null);
        
        return bookRepository.save(book);
    }
    
    /**
     * Calculate penalty for overdue book
     * @param bookId The ID of the book to calculate penalty for
     * @return The penalty amount in dollars (0 if not overdue)
     */
    public double calculatePenalty(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        
        // If book is available or due date is in the future, no penalty
        if (book.isAvailable() || book.getDueDate() == null) {
            return 0.0;
        }
        
        LocalDate today = LocalDate.now();
        LocalDate dueDate = book.getDueDate();
        
        // If not overdue, no penalty
        if (!today.isAfter(dueDate)) {
            return 0.0;
        }
        
        // Calculate days overdue
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
        
        // Calculate penalty
        return daysOverdue * PENALTY_RATE;
    }
    
    /**
     * Generate a unique QR code for a book
     * @return A unique QR code string
     */
    private String generateQrCode() {
        return "LIB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
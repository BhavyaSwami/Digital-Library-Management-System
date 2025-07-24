package com.library.userservice.service;

import com.library.userservice.model.Borrowing;
import com.library.userservice.model.User;
import com.library.userservice.repository.BorrowingRepository;
import com.library.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRepository borrowingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookServiceClient bookServiceClient;
    
    public List<Borrowing> getAllBorrowings() {
        return borrowingRepository.findAll();
    }
    
    public Optional<Borrowing> getBorrowingById(Long id) {
        return borrowingRepository.findById(id);
    }
    
    public List<Borrowing> getBorrowingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return borrowingRepository.findByUser(user);
    }
    
    public List<Borrowing> getActiveBorrowingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return borrowingRepository.findByUserAndReturned(user, false);
    }
    
    public Borrowing borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Check if book is available
        bookServiceClient.getBookById(bookId)
                .filter(book -> book.isAvailable())
                .switchIfEmpty(Mono.error(new RuntimeException("Book is not available")))
                .block();
        
        // Update book availability
        bookServiceClient.updateBookAvailability(bookId, false).block();
        
        // Create borrowing record
        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBookId(bookId);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks loan period
        borrowing.setReturned(false);
        
        return borrowingRepository.save(borrowing);
    }
    
    public Borrowing returnBook(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found with id: " + borrowingId));
        
        if (borrowing.isReturned()) {
            throw new RuntimeException("Book already returned");
        }
        
        // Update book availability
        bookServiceClient.updateBookAvailability(borrowing.getBookId(), true).block();
        
        // Update borrowing record
        borrowing.setReturnDate(LocalDate.now());
        borrowing.setReturned(true);
        
        return borrowingRepository.save(borrowing);
    }
}
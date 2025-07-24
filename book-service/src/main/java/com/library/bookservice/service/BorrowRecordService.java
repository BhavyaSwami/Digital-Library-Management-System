package com.library.bookservice.service;

import com.library.bookservice.model.Book;
import com.library.bookservice.model.BorrowRecord;
import com.library.bookservice.repository.BookRepository;
import com.library.bookservice.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowRecordService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    // Maximum number of books a user can borrow at once
    private static final int MAX_BOOKS_PER_USER = 5;
    
    // Standard loan period in days
    private static final int LOAN_PERIOD_DAYS = 14;
    
    /**
     * Check if a user can borrow more books
     * @param userId The ID of the user
     * @return true if the user can borrow more books, false otherwise
     */
    public boolean canUserBorrow(Long userId) {
        int activeBooks = borrowRecordRepository.countActiveBooksByUser(userId);
        return activeBooks < MAX_BOOKS_PER_USER;
    }
    
    /**
     * Find a book by QR code
     * @param qrCode The QR code of the book
     * @return The book if found, null otherwise
     */
    public Book findBookByQrCode(String qrCode) {
        return bookRepository.findByQrCode(qrCode).orElse(null);
    }
    
    /**
     * Find an active borrow record for a user and book
     * @param userId The ID of the user
     * @param bookId The ID of the book
     * @return The borrow record if found, null otherwise
     */
    public BorrowRecord findActiveBorrowRecord(Long userId, Long bookId) {
        return borrowRecordRepository.findByUserIdAndBookIdAndStatus(userId, bookId, "BORROWED").orElse(null);
    }
    
    /**
     * Borrow a book
     * @param userId The ID of the user borrowing the book
     * @param book The book to borrow
     * @return The borrow record
     */
    @Transactional
    public BorrowRecord borrowBook(Long userId, Book book) {
        // Update book availability
        book.setAvailable(false);
        bookRepository.save(book);
        
        // Create borrow record
        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId);
        record.setBook(book);
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusDays(LOAN_PERIOD_DAYS));
        record.setStatus("BORROWED");
        
        return borrowRecordRepository.save(record);
    }
    
    /**
     * Return a book
     * @param record The borrow record
     * @param penalty The penalty amount (if any)
     * @return The updated borrow record
     */
    @Transactional
    public BorrowRecord returnBook(BorrowRecord record, double penalty) {
        // Update book availability
        Book book = record.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
        
        // Update borrow record
        record.setReturnDate(LocalDateTime.now());
        record.setPenalty(penalty);
        record.setStatus("RETURNED");
        
        return borrowRecordRepository.save(record);
    }
    
    /**
     * Get all active borrowings for a user
     * @param userId The ID of the user
     * @return List of active borrow records
     */
    public List<BorrowRecord> getActiveBorrowings(Long userId) {
        return borrowRecordRepository.findByUserIdAndStatus(userId, "BORROWED");
    }
    
    /**
     * Get all borrowing records for a book
     * @param bookId The ID of the book
     * @return List of borrow records
     */
    public List<BorrowRecord> getBorrowingsByBook(Long bookId) {
        return borrowRecordRepository.findByBookId(bookId);
    }
}
package com.library.bookservice.controller;

import com.library.bookservice.model.Book;
import com.library.bookservice.model.BorrowRecord;
import com.library.bookservice.service.BookService;
import com.library.bookservice.service.BorrowRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Controller", description = "APIs for book operations")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private BorrowRecordService borrowRecordService;

    @PostMapping("/borrow/{userId}/{qrCode}")
    @Operation(summary = "Borrow a book", description = "User borrows a book using QR code")
    public ResponseEntity<?> borrowBook(@PathVariable Long userId, @PathVariable String qrCode) {
        // Check if user can borrow (not reached limit)
        if (!borrowRecordService.canUserBorrow(userId)) {
            return ResponseEntity.badRequest().body("User has reached borrowing limit or has overdue books");
        }

        // Check if book is available
        Book book = borrowRecordService.findBookByQrCode(qrCode);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        if (!book.isAvailable()) {
            return ResponseEntity.badRequest().body("Book is not available for borrowing");
        }

        // Create borrow record
        BorrowRecord record = borrowRecordService.borrowBook(userId, book);

        return ResponseEntity.ok(record);
    }

    @PostMapping("/return/{userId}/{qrCode}")
    @Operation(summary = "Return a book", description = "User returns a borrowed book")
    public ResponseEntity<?> returnBook(@PathVariable Long userId, @PathVariable String qrCode) {
        // Find the book
        Book book = borrowRecordService.findBookByQrCode(qrCode);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        // Find borrow record
        BorrowRecord record = borrowRecordService.findActiveBorrowRecord(userId, book.getId());
        if (record == null) {
            return ResponseEntity.badRequest().body("No active borrowing found for this book and user");
        }

        // Calculate penalty if late
        double penalty = 0.0;
        if (LocalDateTime.now().isAfter(record.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), LocalDateTime.now());
            penalty = daysLate * 0.5; // $0.50 per day late
        }

        // Update record with return date and penalty
        record = borrowRecordService.returnBook(record, penalty);

        Map<String, Object> response = new HashMap<>();
        response.put("borrowRecord", record);
        response.put("penalty", penalty);
        response.put("message",
                penalty > 0 ? "Book returned with a late penalty of $" + penalty : "Book returned successfully");

        return ResponseEntity.ok(response);
    }
}
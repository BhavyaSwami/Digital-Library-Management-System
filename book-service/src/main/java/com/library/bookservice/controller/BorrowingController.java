package com.library.bookservice.controller;

import com.library.bookservice.model.BorrowRecord;
import com.library.bookservice.service.BorrowRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Borrowing Controller", description = "APIs for borrowing operations")
@CrossOrigin(origins = "*")
public class BorrowingController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @GetMapping("/{bookId}/borrowings")
    @Operation(summary = "Get borrowing records for a book", description = "Retrieve all borrowing records for a specific book")
    public ResponseEntity<List<BorrowRecord>> getBorrowingsByBook(@PathVariable Long bookId) {
        List<BorrowRecord> records = borrowRecordService.getBorrowingsByBook(bookId);
        return ResponseEntity.ok(records);
    }
}
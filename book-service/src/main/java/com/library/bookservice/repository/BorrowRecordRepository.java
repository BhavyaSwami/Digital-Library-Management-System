package com.library.bookservice.repository;

import com.library.bookservice.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByUserIdAndStatus(Long userId, String status);

    @Query("SELECT COUNT(b) FROM BorrowRecord b WHERE b.userId = ?1 AND b.status = 'BORROWED'")
    int countActiveBooksByUser(Long userId);

    Optional<BorrowRecord> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status);
    
    @Query("SELECT b FROM BorrowRecord b WHERE b.book.id = ?1")
    List<BorrowRecord> findByBookId(Long bookId);
}
package com.library.userservice.repository;

import com.library.userservice.model.Borrowing;
import com.library.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByUser(User user);
    List<Borrowing> findByUserAndReturned(User user, boolean returned);
    List<Borrowing> findByBookId(Long bookId);
    List<Borrowing> findByBookIdAndReturned(Long bookId, boolean returned);
}
package com.library.bookservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String author;
    private String isbn;
    private Integer publicationYear;
    private String publisher;
    private String genre;
    private String description;
    private boolean available = true;
    private String coverImage;
    
    // Fields for borrowing functionality
    private String qrCode;
    private Long borrowId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
}
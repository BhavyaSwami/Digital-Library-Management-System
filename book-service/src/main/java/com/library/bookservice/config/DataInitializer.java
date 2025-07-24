package com.library.bookservice.config;

import com.library.bookservice.model.Book;
import com.library.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if books already exist
        if (bookRepository.count() == 0) {
            // Create sample books
            // Create book 1
            Book book1 = new Book();
            book1.setTitle("To Kill a Mockingbird");
            book1.setAuthor("Harper Lee");
            book1.setIsbn("978-0446310789");
            book1.setPublicationYear(1960);
            book1.setPublisher("J. B. Lippincott & Co.");
            book1.setGenre("Fiction");
            book1.setDescription("The story of racial injustice and the loss of innocence in the American South.");
            book1.setAvailable(true);
            book1.setCoverImage("https://m.media-amazon.com/images/I/71FxgtFKcQL._AC_UF1000,1000_QL80_.jpg");
            book1.setQrCode("QR-" + book1.getIsbn());
            
            // Create book 2
            Book book2 = new Book();
            book2.setTitle("1984");
            book2.setAuthor("George Orwell");
            book2.setIsbn("978-0451524935");
            book2.setPublicationYear(1949);
            book2.setPublisher("Secker & Warburg");
            book2.setGenre("Dystopian");
            book2.setDescription("A dystopian social science fiction novel that examines the consequences of totalitarianism.");
            book2.setAvailable(true);
            book2.setCoverImage("https://m.media-amazon.com/images/I/71kxa1-0mfL._AC_UF1000,1000_QL80_.jpg");
            book2.setQrCode("QR-" + book2.getIsbn());
            
            // Create book 3
            Book book3 = new Book();
            book3.setTitle("The Great Gatsby");
            book3.setAuthor("F. Scott Fitzgerald");
            book3.setIsbn("978-0743273565");
            book3.setPublicationYear(1925);
            book3.setPublisher("Charles Scribner's Sons");
            book3.setGenre("Fiction");
            book3.setDescription("A novel that examines the American Dream and the Jazz Age.");
            book3.setAvailable(true);
            book3.setCoverImage("https://m.media-amazon.com/images/I/71FTb9X6wsL._AC_UF1000,1000_QL80_.jpg");
            book3.setQrCode("QR-" + book3.getIsbn());
            
            // Create book 4
            Book book4 = new Book();
            book4.setTitle("Pride and Prejudice");
            book4.setAuthor("Jane Austen");
            book4.setIsbn("978-0141439518");
            book4.setPublicationYear(1813);
            book4.setPublisher("T. Egerton, Whitehall");
            book4.setGenre("Romance");
            book4.setDescription("A romantic novel of manners that follows the character development of Elizabeth Bennet.");
            book4.setAvailable(true);
            book4.setCoverImage("https://m.media-amazon.com/images/I/71Q1tPupKjL._AC_UF1000,1000_QL80_.jpg");
            book4.setQrCode("QR-" + book4.getIsbn());
            
            List<Book> books = Arrays.asList(book1, book2, book3, book4)
            );
            
            // Save all books
            bookRepository.saveAll(books);
            
            System.out.println("Sample books have been initialized");
        }
    }
}
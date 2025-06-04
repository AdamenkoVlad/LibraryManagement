package com.example.librarymanagment.repository;

import com.example.librarymanagment.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    Optional<Book> findByTitleAndAuthor(String title, String author);

    @Query("SELECT DISTINCT b.title FROM Book b JOIN b.borrowRecords br WHERE br.returnedAt IS NULL")
    List<String> findAllBorrowedBookTitles();

    @Query("SELECT b.title, COUNT(br) FROM Book b JOIN b.borrowRecords br WHERE br.returnedAt IS NULL GROUP BY b.title")
    List<Object[]> findAllBorrowedBookTitlesWithCount();
}

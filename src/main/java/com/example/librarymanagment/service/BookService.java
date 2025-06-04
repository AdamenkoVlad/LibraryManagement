package com.example.librarymanagment.service;

import com.example.librarymanagment.dto.BookDTO;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.Book;

import java.util.List;


public interface BookService {
    Book createBook(BookDTO bookDTO);

    Book getBookById(Long id) throws ResourceNotFoundException;

    List<Book> getAllBooks();

    Book updateBook(Long id, BookDTO bookDTO) throws ResourceNotFoundException;

    void deleteBook(Long id) throws ResourceNotFoundException;

    List<String> getAllBorrowedBookTitles();

    List<Object[]> getAllBorrowedBookTitlesWithCount();
}

package com.example.librarymanagment.service.imp;

import com.example.librarymanagment.dto.BookDTO;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.Book;
import com.example.librarymanagment.repository.BookRepository;
import com.example.librarymanagment.repository.BorrowRecordRepository;
import com.example.librarymanagment.service.BookService;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BookServiceImpl(BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    public Book createBook(BookDTO bookDTO) {
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor());

        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.increaseAmount();
            return bookRepository.save(book);
        } else {
            Book newBook = new Book();
            newBook.setTitle(bookDTO.getTitle());
            newBook.setAuthor(bookDTO.getAuthor());
            newBook.setAmount(bookDTO.getAmount() != null ? bookDTO.getAmount() : 1);
            return bookRepository.save(newBook);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(Long id) throws ResourceNotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book updateBook(Long id, BookDTO bookDTO) throws ResourceNotFoundException {
        Book book = getBookById(id);
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setAmount(bookDTO.getAmount());
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) throws ResourceNotFoundException {
        Book book = getBookById(id);
        if (borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(id)) {
            throw new IllegalStateException("Cannot delete book with active borrow records");
        }
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllBorrowedBookTitles() {
        return bookRepository.findAllBorrowedBookTitles();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getAllBorrowedBookTitlesWithCount() {
        return bookRepository.findAllBorrowedBookTitlesWithCount();
    }
}
package com.example.librarymanagment.service;

import com.example.librarymanagment.dto.BookDTO;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.Book;
import com.example.librarymanagment.repository.BookRepository;
import com.example.librarymanagment.repository.BorrowRecordRepository;
import com.example.librarymanagment.service.imp.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Кобзар");
        book.setAuthor("Тарас Шевченко");
        book.setAmount(5);

        bookDTO = new BookDTO();
        bookDTO.setTitle("Кобзар");
        bookDTO.setAuthor("Тарас Шевченко");
        bookDTO.setAmount(5);
    }

    @Test
    void createBook_NewBook_ShouldReturnCreatedBook() {
        when(bookRepository.findByTitleAndAuthor(anyString(), anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook(bookDTO);

        assertNotNull(result);
        assertEquals("Кобзар", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_ExistingBook_ShouldIncreaseAmount() {
        when(bookRepository.findByTitleAndAuthor(anyString(), anyString())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook(bookDTO);

        assertNotNull(result);
        assertEquals(6, result.getAmount()); // original amount was 5
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getBookById_ExistingId_ShouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Кобзар", result.getTitle());
    }

    @Test
    void getBookById_NonExistingId_ShouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals("Кобзар", result.get(0).getTitle());
    }

    @Test
    void updateBook_ExistingBook_ShouldReturnUpdatedBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO updateDTO = new BookDTO();
        updateDTO.setTitle("Лісова пісня");
        updateDTO.setAuthor("Леся Українка");
        updateDTO.setAmount(3);

        Book result = bookService.updateBook(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Лісова пісня", result.getTitle());
        assertEquals("Леся Українка", result.getAuthor());
        assertEquals(3, result.getAmount());
    }

    @Test
    void updateBook_NonExistingBook_ShouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, bookDTO));
    }

    @Test
    void deleteBook_NoActiveBorrows_ShouldDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(1L)).thenReturn(false);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void deleteBook_WithActiveBorrows_ShouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.existsByBookIdAndReturnedAtIsNull(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> bookService.deleteBook(1L));
        verify(bookRepository, never()).delete(any());
    }

    @Test
    void getAllBorrowedBookTitles_ShouldReturnTitles() {
        List<String> titles = Arrays.asList("Маруся Чурай", "Енеїда");
        when(bookRepository.findAllBorrowedBookTitles()).thenReturn(titles);

        List<String> result = bookService.getAllBorrowedBookTitles();

        assertEquals(2, result.size());
        assertTrue(result.contains("Маруся Чурай"));
        assertTrue(result.contains("Енеїда"));
    }

    @Test
    void getAllBorrowedBookTitlesWithCount_ShouldReturnTitlesWithCount() {
        Object[] record1 = new Object[]{"Маруся Чурай", 2L};
        Object[] record2 = new Object[]{"Енеїда", 1L};
        List<Object[]> records = Arrays.asList(record1, record2);
        when(bookRepository.findAllBorrowedBookTitlesWithCount()).thenReturn(records);

        List<Object[]> result = bookService.getAllBorrowedBookTitlesWithCount();

        assertEquals(2, result.size());
        assertEquals("Маруся Чурай", result.get(0)[0]);
        assertEquals(2L, result.get(0)[1]);
    }
}

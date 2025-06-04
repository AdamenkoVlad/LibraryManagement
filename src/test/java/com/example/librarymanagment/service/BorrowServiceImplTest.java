package com.example.librarymanagment.service;

import com.example.librarymanagment.config.AppConfig;
import com.example.librarymanagment.dto.BorrowDTO;
import com.example.librarymanagment.exception.BookNotAvailableException;
import com.example.librarymanagment.exception.BorrowLimitExceededException;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.*;
import com.example.librarymanagment.repository.BookRepository;
import com.example.librarymanagment.repository.BorrowRecordRepository;
import com.example.librarymanagment.repository.MemberRepository;
import com.example.librarymanagment.service.imp.BorrowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private BorrowServiceImpl borrowService;

    @Mock
    private AppConfig appConfig;

    private Book book;
    private Member member;
    private BorrowDTO borrowDTO;
    private BorrowRecord borrowRecord;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Кайдашева сім'я");
        book.setAuthor("Іван Нечуй-Левицький");
        book.setAmount(5);

        member = new Member();
        member.setId(1L);
        member.setName("Іван Петренко");
        member.setMembershipDate(LocalDate.now());

        borrowDTO = new BorrowDTO();
        borrowDTO.setBookId(1L);
        borrowDTO.setMemberId(1L);

        borrowRecord = new BorrowRecord();
        borrowRecord.setId(1L);
        borrowRecord.setBook(book);
        borrowRecord.setMember(member);
        borrowRecord.setBorrowedAt(LocalDateTime.now());

    }

    @Test
    void borrowBook_AvailableBookAndUnderLimit_ShouldCreateRecord() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowRecordRepository.findByMemberIdAndReturnedAtIsNull(1L))
                .thenReturn(Collections.emptyList());
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(borrowRecord);

        BorrowRecord result = borrowService.borrowBook(borrowDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository, times(1)).save(book);
        assertEquals(4, book.getAmount()); // початкова кількість — 5
    }

    @Test
    void borrowBook_BookNotAvailable_ShouldThrowException() {
        book.setAmount(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThrows(BookNotAvailableException.class, () -> borrowService.borrowBook(borrowDTO));
        verify(borrowRecordRepository, never()).save(any());
    }

    @Test
    void borrowBook_AtBorrowLimit_ShouldThrowException() {
        List<BorrowRecord> activeBorrows = Arrays.asList(
                new BorrowRecord(), new BorrowRecord(), new BorrowRecord(),
                new BorrowRecord(), new BorrowRecord(), new BorrowRecord(),
                new BorrowRecord(), new BorrowRecord(), new BorrowRecord(),
                new BorrowRecord() // 10 активних позик
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowRecordRepository.findByMemberIdAndReturnedAtIsNull(1L))
                .thenReturn(activeBorrows);

        assertThrows(BorrowLimitExceededException.class, () -> borrowService.borrowBook(borrowDTO));
        verify(borrowRecordRepository, never()).save(any());
    }

    @Test
    void borrowBook_BookNotFound_ShouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowService.borrowBook(borrowDTO));
    }

    @Test
    void borrowBook_MemberNotFound_ShouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowService.borrowBook(borrowDTO));
    }

    @Test
    void returnBook_ActiveBorrow_ShouldUpdateRecord() throws Exception {
        when(borrowRecordRepository.findByBookIdAndMemberIdAndReturnedAtIsNull(1L, 1L))
                .thenReturn(Optional.of(borrowRecord));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(borrowRecord);

        BorrowRecord result = borrowService.returnBook(borrowDTO);

        assertNotNull(result);
        assertNotNull(result.getReturnedAt());
        verify(bookRepository, times(1)).save(book);
        assertEquals(6, book.getAmount()); // початкова кількість була 5
    }

    @Test
    void returnBook_NoActiveBorrow_ShouldThrowException() {
        when(borrowRecordRepository.findByBookIdAndMemberIdAndReturnedAtIsNull(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowService.returnBook(borrowDTO));
    }
}


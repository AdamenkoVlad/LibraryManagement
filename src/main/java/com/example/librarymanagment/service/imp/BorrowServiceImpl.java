package com.example.librarymanagment.service.imp;

import com.example.librarymanagment.config.AppConfig;
import com.example.librarymanagment.dto.BorrowDTO;
import com.example.librarymanagment.exception.BookNotAvailableException;
import com.example.librarymanagment.exception.BorrowLimitExceededException;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.Book;
import com.example.librarymanagment.model.BorrowRecord;
import com.example.librarymanagment.model.Member;
import com.example.librarymanagment.repository.BookRepository;
import com.example.librarymanagment.repository.BorrowRecordRepository;
import com.example.librarymanagment.repository.MemberRepository;
import com.example.librarymanagment.service.BorrowService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final int bookBorrowLimit;

    public BorrowServiceImpl(BookRepository bookRepository,
                             MemberRepository memberRepository,
                             BorrowRecordRepository borrowRecordRepository,
                             AppConfig appConfig) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookBorrowLimit = appConfig.bookBorrowLimit();
    }

    @Override
    public BorrowRecord borrowBook(BorrowDTO borrowDTO) throws ResourceNotFoundException, BookNotAvailableException,
            BorrowLimitExceededException {
        Book book = bookRepository.findById(borrowDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + borrowDTO.getBookId()));

        Member member = memberRepository.findById(borrowDTO.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + borrowDTO.getMemberId()));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available for borrowing");
        }

        List<BorrowRecord> activeBorrows = borrowRecordRepository.findByMemberIdAndReturnedAtIsNull(borrowDTO.getMemberId());
        if (!member.canBorrowMoreBooks(activeBorrows.size(), bookBorrowLimit)) {
            throw new BorrowLimitExceededException("Member has reached the maximum borrow limit of " + bookBorrowLimit);
        }

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setMember(member);
        borrowRecord.setBorrowedAt(LocalDateTime.now());

        book.decreaseAmount();
        bookRepository.save(book);

        return borrowRecordRepository.save(borrowRecord);
    }

    @Override
    public BorrowRecord returnBook(BorrowDTO borrowDTO) throws ResourceNotFoundException {
        BorrowRecord borrowRecord = borrowRecordRepository
                .findByBookIdAndMemberIdAndReturnedAtIsNull(borrowDTO.getBookId(), borrowDTO.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Active borrow record not found"));

        Book book = borrowRecord.getBook();
        book.increaseAmount();
        bookRepository.save(book);

        borrowRecord.setReturnedAt(LocalDateTime.now());
        return borrowRecordRepository.save(borrowRecord);
    }
}
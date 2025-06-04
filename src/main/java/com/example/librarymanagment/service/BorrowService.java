package com.example.librarymanagment.service;

import com.example.librarymanagment.dto.BorrowDTO;
import com.example.librarymanagment.exception.BookNotAvailableException;
import com.example.librarymanagment.exception.BorrowLimitExceededException;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.BorrowRecord;

public interface BorrowService {
    BorrowRecord borrowBook(BorrowDTO borrowDTO) throws ResourceNotFoundException, BookNotAvailableException, BorrowLimitExceededException, BorrowLimitExceededException;
    BorrowRecord returnBook(BorrowDTO borrowDTO) throws ResourceNotFoundException;
}
package com.example.librarymanagment.controller;

import com.example.librarymanagment.dto.BorrowDTO;
import com.example.librarymanagment.exception.BookNotAvailableException;
import com.example.librarymanagment.exception.BorrowLimitExceededException;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.BorrowRecord;
import com.example.librarymanagment.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrow")
@Tag(name = "Borrow Management", description = "Endpoints for managing book borrowing operations")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @Operation(summary = "Borrow a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book borrowed successfully",
                    content = @Content(schema = @Schema(implementation = BorrowRecord.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or business rule violation",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book or member not found",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<BorrowRecord> borrowBook(
            @Parameter(description = "Borrow request details") @Valid @RequestBody BorrowDTO borrowDTO)
            throws ResourceNotFoundException, BookNotAvailableException, BorrowLimitExceededException {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.borrowBook(borrowDTO));
    }

    @Operation(summary = "Return a borrowed book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully",
                    content = @Content(schema = @Schema(implementation = BorrowRecord.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Active borrow record not found",
                    content = @Content)
    })
    @PostMapping("/return")
    public ResponseEntity<BorrowRecord> returnBook(
            @Parameter(description = "Return request details") @Valid @RequestBody BorrowDTO borrowDTO)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(borrowService.returnBook(borrowDTO));
    }
}
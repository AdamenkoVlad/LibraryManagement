package com.example.librarymanagment.dto;

public class BorrowDTO {
    private Long bookId;
    private Long memberId;

    // Constructors
    public BorrowDTO() {
    }

    public BorrowDTO(Long bookId, Long memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
    }

    // Getters
    public Long getBookId() {
        return bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    // Setters
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    // equals(), hashCode(), toString()
    @Override
    public String toString() {
        return "BorrowDTO{" +
                "bookId=" + bookId +
                ", memberId=" + memberId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowDTO borrowDTO = (BorrowDTO) o;
        return java.util.Objects.equals(bookId, borrowDTO.bookId) &&
                java.util.Objects.equals(memberId, borrowDTO.memberId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(bookId, memberId);
    }
}
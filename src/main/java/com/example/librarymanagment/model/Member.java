package com.example.librarymanagment.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate membershipDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BorrowRecord> borrowRecords = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Constructors
    public Member() {
    }

    public Member(String name, LocalDate membershipDate) {
        this.name = name;
        this.membershipDate = membershipDate;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public Set<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public void setBorrowRecords(Set<BorrowRecord> borrowRecords) {
        this.borrowRecords = borrowRecords;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Business Logic Methods
    public boolean canBorrowMoreBooks(int currentBorrowCount, int bookBorrowLimit) {
        return currentBorrowCount < bookBorrowLimit;
    }

    public boolean hasBorrowedBooks() {
        return !borrowRecords.isEmpty();
    }


}
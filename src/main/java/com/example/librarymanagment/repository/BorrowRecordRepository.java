package com.example.librarymanagment.repository;

import com.example.librarymanagment.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByMemberIdAndReturnedAtIsNull(Long memberId);

    boolean existsByBookIdAndReturnedAtIsNull(Long bookId);

    Optional<BorrowRecord> findByBookIdAndMemberIdAndReturnedAtIsNull(Long bookId, Long memberId);
}
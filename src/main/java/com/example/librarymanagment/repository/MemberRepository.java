package com.example.librarymanagment.repository;

import com.example.librarymanagment.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByName(String name);

    @Query("SELECT b.title FROM Member m JOIN m.borrowRecords br JOIN br.book b WHERE m.name = :memberName AND br.returnedAt IS NULL")
    List<String> findBorrowedBookTitlesByMemberName(String memberName);

}

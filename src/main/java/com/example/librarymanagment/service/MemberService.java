package com.example.librarymanagment.service;

import com.example.librarymanagment.dto.MemberDTO;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.Member;

import java.util.List;

public interface MemberService {
    Member createMember(MemberDTO memberDTO);

    Member getMemberById(Long id) throws ResourceNotFoundException;

    List<Member> getAllMembers();

    Member updateMember(Long id, MemberDTO memberDTO) throws ResourceNotFoundException;

    void deleteMember(Long id) throws ResourceNotFoundException;

    List<String> getBorrowedBooksByMemberName(String memberName);
}
package com.example.librarymanagment.service.imp;

import com.example.librarymanagment.dto.MemberDTO;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.Member;
import com.example.librarymanagment.repository.BorrowRecordRepository;
import com.example.librarymanagment.repository.MemberRepository;
import com.example.librarymanagment.service.MemberService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public MemberServiceImpl(MemberRepository memberRepository, BorrowRecordRepository borrowRecordRepository) {
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    public Member createMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setMembershipDate(LocalDate.now());
        return memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(Long id) throws ResourceNotFoundException {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member updateMember(Long id, MemberDTO memberDTO) throws ResourceNotFoundException {
        Member member = getMemberById(id);
        member.setName(memberDTO.getName());
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) throws ResourceNotFoundException {
        Member member = getMemberById(id);
        if (member.hasBorrowedBooks()) {
            throw new IllegalStateException("Cannot delete member with borrowed books");
        }
        memberRepository.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getBorrowedBooksByMemberName(String memberName) {
        return memberRepository.findBorrowedBookTitlesByMemberName(memberName);
    }
}
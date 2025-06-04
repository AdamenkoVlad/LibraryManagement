package com.example.librarymanagment.service;
import com.example.librarymanagment.dto.MemberDTO;
import com.example.librarymanagment.exception.ResourceNotFoundException;
import com.example.librarymanagment.model.Member;
import com.example.librarymanagment.repository.BorrowRecordRepository;
import com.example.librarymanagment.repository.MemberRepository;
import com.example.librarymanagment.service.imp.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("Іван Франко");
        member.setMembershipDate(LocalDate.now());

        memberDTO = new MemberDTO();
        memberDTO.setName("Іван Франко");
    }

    @Test
    void createMember_ShouldReturnCreatedMember() {
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member result = memberService.createMember(memberDTO);

        assertNotNull(result);
        assertEquals("Іван Франко", result.getName());
        assertNotNull(result.getMembershipDate());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void getMemberById_ExistingId_ShouldReturnMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member result = memberService.getMemberById(1L);

        assertNotNull(result);
        assertEquals("Іван Франко", result.getName());
    }

    @Test
    void getMemberById_NonExistingId_ShouldThrowException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.getMemberById(1L));
    }

    @Test
    void getAllMembers_ShouldReturnAllMembers() {
        List<Member> members = Arrays.asList(member);
        when(memberRepository.findAll()).thenReturn(members);

        List<Member> result = memberService.getAllMembers();

        assertEquals(1, result.size());
        assertEquals("Іван Франко", result.get(0).getName());
    }

    @Test
    void updateMember_ExistingMember_ShouldReturnUpdatedMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO updateDTO = new MemberDTO();
        updateDTO.setName("Леся Українка");

        Member result = memberService.updateMember(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Леся Українка", result.getName());
    }

    @Test
    void updateMember_NonExistingMember_ShouldThrowException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.updateMember(1L, memberDTO));
    }

    @Test
    void deleteMember_NoBorrowedBooks_ShouldDeleteMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowRecordRepository.findByMemberIdAndReturnedAtIsNull(1L)).thenReturn(Collections.emptyList());

        memberService.deleteMember(1L);

        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    void deleteMember_WithBorrowedBooks_ShouldThrowException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowRecordRepository.findByMemberIdAndReturnedAtIsNull(1L)).thenReturn(Arrays.asList(null));

        assertThrows(IllegalStateException.class, () -> memberService.deleteMember(1L));
        verify(memberRepository, never()).delete(any());
    }

    @Test
    void getBorrowedBooksByMemberName_ShouldReturnBookTitles() {
        List<String> titles = Arrays.asList("Захар Беркут", "Мойсей");
        when(memberRepository.findBorrowedBookTitlesByMemberName("Іван Франко")).thenReturn(titles);

        List<String> result = memberService.getBorrowedBooksByMemberName("Іван Франко");

        assertEquals(2, result.size());
        assertTrue(result.contains("Захар Беркут"));
        assertTrue(result.contains("Мойсей"));
    }
}

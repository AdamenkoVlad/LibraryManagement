package com.example.librarymanagment.controller;

import com.example.librarymanagment.dto.MemberDTO;
import com.example.librarymanagment.model.Member;
import com.example.librarymanagment.service.MemberService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member Management", description = "Endpoints for managing library members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "Create a new member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member created successfully",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        Member createdMember = memberService.createMember(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }

    @Operation(summary = "Get a member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member found",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "404", description = "Member not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(
            @Parameter(description = "ID of the member to be retrieved") @PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @Operation(summary = "Get all members")
    @ApiResponse(responseCode = "200", description = "List of all members",
            content = @Content(schema = @Schema(implementation = Member.class)))
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @Operation(summary = "Update a member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Member not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(
            @Parameter(description = "ID of the member to be updated") @PathVariable Long id,
            @Valid @RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok(memberService.updateMember(id, memberDTO));
    }

    @Operation(summary = "Delete a member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "409", description = "Member cannot be deleted (has borrowed books)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
            @Parameter(description = "ID of the member to be deleted") @PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all borrowed books by member name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of borrowed book titles"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{name}/borrowed-books")
    public ResponseEntity<List<String>> getBorrowedBooksByMemberName(
            @Parameter(description = "Name of the member") @PathVariable String name) {
        return ResponseEntity.ok(memberService.getBorrowedBooksByMemberName(name));
    }
}
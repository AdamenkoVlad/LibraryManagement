package com.example.librarymanagment.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate; // Import LocalDate

public class MemberDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate membershipDate;

    // Constructors
    public MemberDTO() {
    }

    public MemberDTO(Long id, String name, LocalDate membershipDate) {
        this.id = id;
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

    // equals(), hashCode(), toString()
    @Override
    public String toString() {
        return "MemberDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", membershipDate=" + membershipDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDTO memberDTO = (MemberDTO) o;
        return java.util.Objects.equals(id, memberDTO.id) &&
                java.util.Objects.equals(name, memberDTO.name) &&
                java.util.Objects.equals(membershipDate, memberDTO.membershipDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, membershipDate);
    }
}
package com.example.librarymanagment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BookDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    @Pattern(regexp = "^[A-Z].*", message = "Title must start with a capital letter")
    private String title;

    @NotBlank(message = "Author is required")
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$",
            message = "Author must be in format 'Name Surname' with capital letters")
    private String author;

    private Integer amount;

    // Constructors
    public BookDTO() {
    }

    public BookDTO(Long id, String title, String author, Integer amount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.amount = amount;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getAmount() {
        return amount;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    // equals(), hashCode(), toString() - Зазвичай генеруються `@Data`
    // Ви можете згенерувати їх в IDE або реалізувати вручну, якщо вони потрібні.
    // Приклад toString():
    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", amount=" + amount +
                '}';
    }

    // Приклад equals() та hashCode():
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return java.util.Objects.equals(id, bookDTO.id) &&
                java.util.Objects.equals(title, bookDTO.title) &&
                java.util.Objects.equals(author, bookDTO.author) &&
                java.util.Objects.equals(amount, bookDTO.amount);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title, author, amount);
    }
}
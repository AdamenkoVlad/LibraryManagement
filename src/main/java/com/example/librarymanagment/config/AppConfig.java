package com.example.librarymanagment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${member.book.borrow.limit:10}")
    private int bookBorrowLimit;

    @Bean
    public int bookBorrowLimit() {
        return bookBorrowLimit;
    }
}

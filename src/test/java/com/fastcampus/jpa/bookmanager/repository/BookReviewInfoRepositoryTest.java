package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.Book;
import com.fastcampus.jpa.bookmanager.domain.BookReviewInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReviewInfoRepositoryTest {

    @Autowired
    BookReviewInfoRepository bookReviewInfoRepository;

    @Autowired
    BookRepository bookRepository;

    @Test
    void saveBooKAndSaveReviewInfo() {
        Book book = new Book();
        book.setName("자바의 정석");
        book.setCategory("프로그래밍");
        bookRepository.save(book);

        System.out.println(">>> " + bookRepository.findAll());

        BookReviewInfo bookReviewInfo = new BookReviewInfo();
        bookReviewInfo.setReviewCount(2);
        bookReviewInfo.setAverageReviewScore(4.3f);
        bookReviewInfo.setBook(bookRepository.findById(1L).orElseThrow(RuntimeException::new));
        bookReviewInfoRepository.save(bookReviewInfo);

        System.out.println(">>> " + bookReviewInfoRepository.findAll());
    }
}
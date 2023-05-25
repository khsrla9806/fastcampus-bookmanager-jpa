package com.fastcampus.jpa.bookmanager.service;

import com.fastcampus.jpa.bookmanager.repository.AuthorRepository;
import com.fastcampus.jpa.bookmanager.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void checked_exception이_발생했을때_Roll_Back_테스트() {
        try {
            bookService.putBookAndAuthor();
        } catch (Exception e) {
            System.out.println(">>> " + e.getMessage());
        }

        System.out.println("books: " + bookRepository.findAll());
        System.out.println("authors: " + authorRepository.findAll());

        // 결론: @Transactional(rollbackFor = Exception.class)를 하지 않으면 Roll Back되지 않는다.
    }

    @Test
    void 스프링빈_클래스_내부에_있는_Transactional메서드를_다른_메서드를_통해_호출했을때_테스트() {
        bookService.methodUsingTransactionalMethodInBeanClass();
        System.out.println("books: " + bookRepository.findAll());
        System.out.println("authors: " + authorRepository.findAll());

        // 결론: RuntimeException이 발생하지만 Roll Back이 되지 않는다.
    }
}
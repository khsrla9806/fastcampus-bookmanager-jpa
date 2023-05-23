package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.Author;
import com.fastcampus.jpa.bookmanager.domain.Book;
import com.fastcampus.jpa.bookmanager.domain.BookAndAuthor;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookAndAuthorRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookAndAuthorRepository bookAndAuthorRepository;

    @Test
    @Transactional
    void bookAndAuthorRelationTest() {
        Book book1 = givenBook("1번 책");
        Book book2 = givenBook("2번 책");
        Book book3 = givenBook("3번 책 - 2명의 작가");

        Author author1 = givenAuthor("김작가 - (1번)");
        Author author2 = givenAuthor("오작가 - (2번, 3번)");
        Author author3 = givenAuthor("박작가 - (3번)");

        BookAndAuthor bookAndAuthor1 = givenBookAndAuthor(book1, author1);
        BookAndAuthor bookAndAuthor2 = givenBookAndAuthor(book2, author2);
        BookAndAuthor bookAndAuthor3 = givenBookAndAuthor(book3, author2);
        BookAndAuthor bookAndAuthor4 = givenBookAndAuthor(book3, author3);

        book1.addBookAndAuthor(bookAndAuthor1);
        book2.addBookAndAuthor(bookAndAuthor2);
        book3.addBookAndAuthor(bookAndAuthor3, bookAndAuthor4);
        bookRepository.saveAll(Lists.newArrayList(book1, book2, book3));

        author1.addBookAndAuthor(bookAndAuthor1);
        author2.addBookAndAuthor(bookAndAuthor2, bookAndAuthor3);
        author3.addBookAndAuthor(bookAndAuthor4);
        authorRepository.saveAll(Lists.newArrayList(author1, author2, author3));

        // 조회
        bookAndAuthorRepository.findAll().forEach(System.out::println);
        System.out.println("=========== 책 검색 ============");
        Book foundedBook3 = bookRepository.findFirstByName("3번 책 - 2명의 작가");
        bookRepository.findById(foundedBook3.getId()).get().getBookAndAuthors().forEach(System.out::println);
    }

    private Book givenBook(String name) {
        Book book = new Book();
        book.setName(name);

        return bookRepository.save(book);
    }

    private Author givenAuthor(String name) {
        Author author = new Author();
        author.setName(name);

        return authorRepository.save(author);
    }

    private BookAndAuthor givenBookAndAuthor(Book book, Author author) {
        BookAndAuthor bookAndAuthor = new BookAndAuthor();
        bookAndAuthor.setBook(book);
        bookAndAuthor.setAuthor(author);

        return bookAndAuthorRepository.save(bookAndAuthor);
    }
}
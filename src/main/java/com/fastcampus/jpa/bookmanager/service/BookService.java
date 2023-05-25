package com.fastcampus.jpa.bookmanager.service;

import com.fastcampus.jpa.bookmanager.domain.Author;
import com.fastcampus.jpa.bookmanager.domain.Book;
import com.fastcampus.jpa.bookmanager.repository.AuthorRepository;
import com.fastcampus.jpa.bookmanager.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public void methodUsingTransactionalMethodInBeanClass() {
        this.transactionalMethodInBeanClass();
    }

    @Transactional
    public void transactionalMethodInBeanClass() {
        Book book = new Book();
        book.setName("JPA 시작하기");
        bookRepository.save(book);

        Author author = new Author();
        author.setName("rody");
        authorRepository.save(author);
    }

    @Transactional(rollbackFor = Exception.class) // checked exception도 roll-back 시키기 위함
    public void putBookAndAuthor() throws Exception {
        Book book = new Book();
        book.setName("JPA 시작하기");
        bookRepository.save(book);

        Author author = new Author();
        author.setName("rody");
        authorRepository.save(author);
        throw new Exception("checked exception을 던집니다.");
        // @Transactional을 붙이면 throw가 발생하면 원자성에 의해서 commit이 되지 않는다.
        // (주의) 하지만 check exception이 발생하는 경우에는 roll back되지 않습니다.
    }
}

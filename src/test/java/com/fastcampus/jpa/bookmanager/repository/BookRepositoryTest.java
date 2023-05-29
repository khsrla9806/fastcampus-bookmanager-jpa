package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.Book;
import com.fastcampus.jpa.bookmanager.domain.Publisher;
import com.fastcampus.jpa.bookmanager.domain.Review;
import com.fastcampus.jpa.bookmanager.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional // Lazy Fetch에 대한 오류를 해결하기 위해 추가 (일단 추가 추후 설명)
    void bookRelationTest() {
        // Given
        givenBookAndReview();

        // When & Then
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        System.out.println("Reviews : " + user.getReviews());
        System.out.println("Book : " + user.getReviews().get(0).getBook());
        System.out.println("Publisher : " + user.getReviews().get(0).getBook().getPublisher());
    }

    @Test
    void softDeleteTest() {
        // deleted false만 출력되는지 확인
        bookRepository.findAll().forEach(System.out::println);
    }

    @Test
    void cascadeRemoveTest() {
        Book book = bookRepository.findById(1L).get();
        bookRepository.delete(book);

        publisherRepository.findAll().forEach(System.out::println);

        // 결론 : id 1번 Book을 삭제하면 관련된 1번 J 출판사도 함께 삭제됨.
        // 알 수 있는 내용 : 만약 2개 이상의 Book이 같은 출판사와 연관이 있다면 1개의 Book을 삭제하면 SQLException이 발생
    }

    @Test
    void orphanRemovalTestAndCompareWithCascadeRemove() {
        // Given
        Publisher publisher = new Publisher();
        publisher.setName("J 출판사");

        Book book1 = new Book();
        book1.setName("JPA의 정석");

        Book book2 = new Book();
        book2.setName("JAVA의 정석");

        publisher.addBook(book1);
        publisher.addBook(book2);

        publisherRepository.save(publisher);

        // When : 부모인 Publisher에서 Book을 삭제한다면?
        Publisher founedPublisher = publisherRepository.findById(1L).get();
        founedPublisher.getBooks().remove(0);

        // Then
        System.out.println(">>> " + bookRepository.findAll());
        System.out.println(">>> " + publisherRepository.findAll());


        /*
            [ 결론 ]
            - CascadeType.REMOVE와 orphanRemoval = true 모두 부모가 삭제될 때, 자식이 삭제되는 것은 동일
            - CascadeType.REMOVE에서는 부모 엔티티에서 자식을 삭제하더라도, 해당 자식이 삭제되지는 않음. 즉, 고아 엔티티를 삭제하지 않음
            - orphanRemoval = true에서는 부모에서 자식을 삭제하면, 자식 엔티티는 고아가 되기 때문에 이를 모두 제거해줌
        */
    }

    private void givenBookAndReview() {
        givenReview(givenUser(), givenBook(givenPublisher()));
    }

    private User givenUser() {
        return userRepository.findById(1L).orElseThrow(RuntimeException::new);
    }

    private Book givenBook(Publisher publisher) {
        Book book = new Book();
        book.setName("자바의 정석");
        book.setCategory("프로그래밍");
        book.setPublisher(publisher);

        return bookRepository.save(book);
    }

    private Publisher givenPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("패스트 출판사");

        return publisherRepository.save(publisher);
    }

    private void givenReview(User user, Book book) {
        Review review = new Review();
        review.setTitle("공부하기 좋은 책");
        review.setContent("추천하는 책입니다.");
        review.setScore(5.0f);
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);
    }

}

package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.Book;
import com.fastcampus.jpa.bookmanager.domain.Publisher;
import com.fastcampus.jpa.bookmanager.domain.Review;
import com.fastcampus.jpa.bookmanager.domain.User;
import com.fastcampus.jpa.bookmanager.repository.dto.BookStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Test
    void queryTest() {
        Book book = new Book();
        book.setName("JPA의 정석");
        bookRepository.save(book);

        System.out.println(">>>"
                + bookRepository.findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(
                        "JPA의 정석", LocalDateTime.now().minusDays(1L), LocalDateTime.now().minusDays(1L)
        ));

        System.out.println(">>> "
                + bookRepository.findByNameRecently(
                "JPA의 정석", LocalDateTime.now().minusDays(1L), LocalDateTime.now().minusDays(1L)
        ));
    }

    @Test
    void nativeQueryTest() {
        Book book1 = new Book();
        book1.setName("JPA의 정석");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setName("JAVA의 정석");
        bookRepository.save(book2);

        bookRepository.updateCategories();
        System.out.println(">>> " + bookRepository.findAllCustom());

        /*
            Native Query를 사용하면 특정 DB에 의존하는 개발을 하기 때문에 많이 사용하는 것은 좋지 않을 수 있다.
            하지만 성능적인면에서 Native Query를 사용하기도 한다.
            - JPA를 거쳐 지나가면 select를 실행하고, delete를 실행하고 이런 경우가 많다.
            - 이렇게 되면 삭제를 할 때마다 select를 수행하고, 삭제를 진행하기 떄문에 성능이 많이 떨어진다.
            - 이때는 한번의 쿼리를 사용해서 할 수 있도록 Native Query를 사용하는 경우가 있다.
         */
    }

    @Test
    void converterTest() {
        Book book1 = new Book();
        book1.setName("JPA의 정석");
        book1.setStatus(new BookStatus(100));
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setName("JAVA의 정석");
        book2.setStatus(new BookStatus(200));
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setName("SPRING의 정석");
        book3.setStatus(new BookStatus(100));
        bookRepository.save(book3);

        // DB에 int로 저장이 되었는지 Native 쿼리를 사용
        System.out.println(">>> " + bookRepository.findRawRecord().values());
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

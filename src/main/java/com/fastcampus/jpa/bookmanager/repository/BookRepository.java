package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findFirstByName(String name);

    /**
     * 쿼리 메서드가 너무 길어지는 상황이 발생할 수 있다.
     * 이때는 @Query를 사용하는 것이 더 좋을 수도 있다.
     */
    List<Book> findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(String name, LocalDateTime createdAt, LocalDateTime updatedAt);

    @Query(value = "select b from Book b where name = :name and createdAt >= :createdAt and updatedAt >= :updatedAt and category is null")
    List<Book> findByNameRecently(
            @Param("name") String name,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    // Native Query 사용하기
    @Query(value = "select * from book", nativeQuery = true)
    List<Book> findAllCustom();

    /*
        Native Query를 사용하면 Entity에 사용했던 @Where는 사용하지 못한다.
     */
    @Transactional
    @Modifying
    @Query(value = "update book set category='개발 서적'", nativeQuery = true)
    int updateCategories();
}

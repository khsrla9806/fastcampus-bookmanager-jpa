package com.fastcampus.jpa.bookmanager.domain;

import com.fastcampus.jpa.bookmanager.domain.listener.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Where(clause = "deleted = false")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;

    private boolean deleted;

    @OneToOne(mappedBy = "book")
    @ToString.Exclude // 순환참조 문제 = 양방향성 참조에 의해서 발생
    private BookReviewInfo bookReviewInfo;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn
    private Publisher publisher;

    // @ManyToMany
    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    public void addBookAndAuthor(BookAndAuthor... bookAndAuthor) {
        Collections.addAll(this.bookAndAuthors, bookAndAuthor);
    }
}

package com.fastcampus.jpa.bookmanager.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class BookReviewInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기본값은 optional = true : 있을 수도 있고 없을 수도 있는 값 (outer join)
    // book은 절대 null을 허용하지 않는다.
    @OneToOne(optional = false)
    private Book book;
    private float averageReviewScore; // 기본형 타입은 Not Null로 DB가 생성됨
    private int reviewCount;
}

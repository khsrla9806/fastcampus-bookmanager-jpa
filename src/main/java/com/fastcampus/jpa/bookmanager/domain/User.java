package com.fastcampus.jpa.bookmanager.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user", indexes = {@Index(columnList = "name")}, uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @Enumerated(value = EnumType.STRING) // default가 Ordinal
    private Gender gender;
    @Column(updatable = false) // update 쿼리에서 제외
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Transient // 영속 대상에서 제외
    private String testData;
}

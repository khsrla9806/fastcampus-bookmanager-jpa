package com.fastcampus.jpa.bookmanager.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(value = MyEntityListener.class)
public class UserHistory implements Auditable {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

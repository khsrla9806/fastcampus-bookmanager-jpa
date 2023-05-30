package com.fastcampus.jpa.bookmanager.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass // 상속받는 클래스의 필드로서 DB에 Mapping될 수 있도록 설정
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "datetime(6) default now(6)")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "datetime(6) default now(6)")
    private LocalDateTime updatedAt;
}

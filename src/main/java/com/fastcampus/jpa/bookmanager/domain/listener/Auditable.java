package com.fastcampus.jpa.bookmanager.domain.listener;

import java.time.LocalDateTime;

public interface Auditable {
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    void setCreatedAt(LocalDateTime time);
    void setUpdatedAt(LocalDateTime time);
}

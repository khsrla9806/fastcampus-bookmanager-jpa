package com.fastcampus.jpa.bookmanager.domain.listener;

import com.fastcampus.jpa.bookmanager.domain.User;
import com.fastcampus.jpa.bookmanager.domain.UserHistory;
import com.fastcampus.jpa.bookmanager.repository.UserHistoryRepository;
import com.fastcampus.jpa.bookmanager.support.BeanUtils;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class UserEntityListener {
    @PostPersist
    @PostUpdate
    public void prePersistAndPreUpdate(Object o) {
        // userHistoryRepository를 BeanUtils를 통해 주입받음
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        User user = (User) o;
        UserHistory userHistory = new UserHistory();
        userHistory.setUser(user);
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());

        userHistoryRepository.save(userHistory);
    }
}

package com.fastcampus.jpa.bookmanager.service;

import com.fastcampus.jpa.bookmanager.domain.User;
import com.fastcampus.jpa.bookmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Service
public class UserService {

    private final EntityManager entityManager;
    private final UserRepository userRepository;

    @Transactional
    public void persistStatus() {
        User user = new User();
        user.setName("NewUser");
        user.setEmail("NewUser@test.com");

        entityManager.persist(user); // user 엔티티를 영속상태로 변경 (영속성 컨텍스트에서 관리함 - setter 변경 o)
        entityManager.detach(user); // user 엔티티를 준영속상태로 변경 (영속성 컨텍스트에서 더이상 관리하지 않음 - setter 변경 x)

        user.setName("UpdateNewUser");
        entityManager.merge(user); // DB 영속상태로 변경 (setter 변경 재허용, 이 줄이 없으면 user의 이름은 변경되지 않음)

        User user1 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        entityManager.remove(user1); // user1 엔티티를 비영속상태로 변경
    }
}

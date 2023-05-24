package com.fastcampus.jpa.bookmanager.service;

import com.fastcampus.jpa.bookmanager.domain.User;
import com.fastcampus.jpa.bookmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class EntityManagerTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 트랜잭션_없을때_이름으로_동일유저조회() {
        System.out.println(userRepository.findAllByName("hoon").get(0));
        System.out.println(userRepository.findAllByName("hoon").get(0));
        System.out.println(userRepository.findAllByName("hoon").get(0));

        // 결과 : select 쿼리가 3번 출력
    }

    @Test
    @Transactional
    void 트랜잭션_있을때_이름으로_동일유저조회() {
        System.out.println(userRepository.findAllByName("hoon").get(0));
        System.out.println(userRepository.findAllByName("hoon").get(0));
        System.out.println(userRepository.findAllByName("hoon").get(0));

        // 결과 : select 쿼리가 3번 출력
    }

    @Test
    void 트랜잭션_없을때_Id로_동일유저조회() {
        System.out.println(userRepository.findById(1L).get());
        System.out.println(userRepository.findById(1L).get());
        System.out.println(userRepository.findById(1L).get());

        // 결과 : select 쿼리가 3번 출력
    }

    @Test
    @Transactional
    void 트랜잭션_있을때_Id로_동일유저조회() {
        System.out.println(userRepository.findById(1L).get()); // 이때 한 번 select를 하고, 영속성 컨테이너에 캐싱
        System.out.println(userRepository.findById(1L).get());
        System.out.println(userRepository.findById(1L).get());

        // 결과 : select 쿼리가 1번 출력 (Id로 조회할 때는 캐싱이 일어난다)
    }

    @Test
    void 트랜잭션_없을때_Update_쿼리실행시점() {
        User user = userRepository.findById(1L).get();

        user.setName("modifiedHoon");
        userRepository.save(user);
        System.out.println(">> 이름 수정완료");

        user.setEmail("modifiedHoon@naver.com");
        userRepository.save(user);
        System.out.println(">> 이메일 수정완료");

        // 결과 : save() 메서드 호출 때마다 1번씩 update 쿼리를 실행
        // 추가 : 첫 번째 findById()에서 캐싱되지 않기 때문에 각 save() 메서드마다 select 쿼리도 1번씩 실행
    }

    @Test
    @Transactional
    void 트랜잭션_있을때_Update_쿼리실행시점() {
        User user = userRepository.findById(1L).get();

        user.setName("modifiedHoon");
        userRepository.save(user);
        System.out.println(">> 이름 수정완료");

        user.setEmail("modifiedHoon@naver.com");
        userRepository.save(user);
        System.out.println(">> 이메일 수정완료");

        userRepository.flush(); // @Test에서 @Transactional 사용 시 RollBack이 일어나기 때문에 마지막에 수동으로 DB 반영

        // 결과 : 마지막에 한번 update 쿼리가 실행
        // 추가 : 첫 번째 findById()에서 캐싱되었기 때문에 마지막 flush()에서 다시 select 쿼리를 실행하지 않음. (성능 향상)
    }

    @Test
    @Transactional
    void 복잡한조회조건에_JPQL_쿼리가_실행시_Flush() {
        User user = userRepository.findById(1L).get();

        user.setName("modifiedHoon");
        userRepository.save(user);
        System.out.println(">> 이름 수정완료"); // DB에 수정내용 반영 전

        System.out.println("======= DB에서 모든 유저 정보를 가져옴 =======");
        userRepository.findAll().forEach(System.out::println);

        // 결과 : flush()를 수행하지 않았는데, findAll()이 호출되었을 때, update 쿼리가 실행됨.
        //       영속성 캐시에 존재하는 내용이 아직 DB에 반영되지 않았기 때문에
        //       findAll()로 가져오게 되면 DB에 있는 값과 수정된 값이 다른 상황이 발생
        //       이런 문제를 해결하기 위해 JPQL이나 복잡한 조회가 실행되기 전에 영속성 캐시에 있는 내용을 모두 DB에 반영한 후 쿼리 실행
    }
}

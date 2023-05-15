package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void getUsersByNothingAndId() {
        List<User> users1 = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        users1.forEach(System.out::println);

        List<User> users2 = userRepository.findAllById(Lists.newArrayList(1L, 3L, 5L));
        users2.forEach(System.out::println);
    }

    @Test
    void saveUsersWithNameAndEmail() {
        // Given
        User user1 = new User("test1", "test1@naver.com");
        User user2 = new User("test2", "test2@naver.com");

        // When
        userRepository.saveAll(Lists.newArrayList(user1, user2));

        // Then
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    @Test
    @Transactional // getOne()은 Entity에 대한 Lazy Fetch를 제공하기 떄문에 세션 유지가 필요
    void getOneUserById() {
        User entity = userRepository.save(new User("test", "test@naver.com"));
        User user = userRepository.getOne(entity.getId());
        System.out.println(user);
    }

    @Test
    void findExistingUserById() {
        Optional<User> user = userRepository.findById(1L); // Eager Fetch 방식을 사용
        System.out.println(user.orElse(null));
    }

    @Test
    void getCount() {
        long count = userRepository.count();
        System.out.println(count);
    }

    @Test
    void deleteUserByEntity() {
        userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new));
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    @Test
    void deleteUserById() {
        User entity = userRepository.save(new User("test", "test@naver.com"));
        // 내부적으로 Select 쿼리를 실행해서 해당 Entity의 존재 여부를 확인
        userRepository.deleteById(entity.getId());
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    @Test
    void deleteAllUsers() {
        // delete 쿼리 전에 Select 쿼리를 실행한다.
        // deleteInBatch 또는 deleteAllInBatch를 쓰면 Delete 전에 Select를 쓰지 않는다.
        userRepository.deleteAll();
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    @Test
    void page() {
        Page<User> users = userRepository.findAll(PageRequest.of(1, 3));

        System.out.println("page : " + users);
        System.out.println("totalElements : " + users.getTotalElements());
        System.out.println("totalPages : " + users.getTotalPages());
        System.out.println("numberOfElements : " + users.getNumberOfElements());
        System.out.println("sort : " + users.getSort());
        System.out.println("size : " + users.getSize());

        users.getContent().forEach(System.out::println);
    }

    @Test
    void getUsersUsingExampleMatcherWithIgnore() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name") // name 필드는 매칭에서 제거
                .withMatcher("email", endsWith());

        Example<User> example = Example.of(new User("ignore", "kakao.com"), matcher);

        userRepository.findAll(example).forEach(System.out::println);
    }

    @Test
    void getUsersUsingExampleMatcherWithExactMatching() {
        // 실제로는 ExampleMatcher보다는 QueryDsl을 많이 사용한다.
        Example<User> example = Example.of(new User("hoon", "hoon@naver.com"));

        userRepository.findAll(example).forEach(System.out::println);
    }
}
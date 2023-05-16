package com.fastcampus.jpa.bookmanager.repository;

import com.fastcampus.jpa.bookmanager.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Select : find, get, read, query, search, stream 등 접두어를 사용할 수 있다.
     * First, Top 등과 같이 쓰면 앞에서 부터 원하는 개수의 Entity를 얻어올 수 있다. (where에 limit 구문이 추가)
     */
    List<User> findAllByName(String name);
    List<User> findAllByNameIs(String name); // 위에 쿼리 메소드와 동일한 기능 (단지 가독성 추가)
    List<User> findAllByNameEquals(String name); // 위에 쿼리 메소드와 동일한 기능 (단지 가독성 추가)
    Optional<User> findByName(String name);
    Optional<User> findFirst1ByName(String name); // 가장 처음 발견된 유저를 반환
    Optional<User> findByEmail(String email);

    // 여러 개의 where절 조건
    List<User> findByNameAndEmail(String name, String email);
    List<User> findByNameOrEmail(String name, String email);

    // 날짜 관련된 where절 조건 (After, GreaterThan, Before, LessThan)
    List<User> findByCreatedAtAfter(LocalDateTime day); // day 이후의 날짜 = Equal를 포함하지 않음
    List<User> findByCreatedAtGreaterThan(LocalDateTime day); // day 이후의 날짜 (초과)
    List<User> findByCreatedAtGreaterThanEqual(LocalDateTime day); // day 이후의 날짜 (이상)
    List<User> findByCreatedAtBefore(LocalDateTime day); // day 이전의 날짜 = Equal를 포함하지 않음
    List<User> findByCreatedAtLessThan(LocalDateTime day); // day 이전의 날짜 (미만)
    List<User> findByCreatedAtLessThanEqual(LocalDateTime day); // day 이전의 날짜 (이하)
    List<User> findByCreatedAtBetween(LocalDateTime day1, LocalDateTime day2); // day1 이상 day2 이하의 시간 = Equal 포함

    // IS_EMPTY는 String이 대상이 아니고 Collection이 대상이다.
    List<User> findByNameIsNotNull(); // name이 Null이 아닌 User들을 모두 출력
    List<User> findByNameIsNull(); // name이 Null인 애들만 모두 출력
    List<User> findByNameIn(List<String> names); // names 안에 포함된 name을 가진 엔티티를 가져옴

    // 문자열 관련 연산 (LIKE)
    List<User> findByNameStartingWith(String name); // name%
    List<User> findByNameEndingWith(String name); // %name
    List<User> findByNameContains(String name); // %name%
    List<User> findByNameLike(String name); // %를 직접 넣어줘야함

    // 정렬하기 (Order By + Asc, Desc)
    List<User> findTopByName(String name); // 가장 먼저 나오는 엔티티 반환
    List<User> findTopByNameOrderByIdDesc(String name); // id로 내림차순 정렬하여 가장 먼저 나오는 엔티티 반환 (Last1은 없기 떄문에)
    List<User> findFirstByNameOrderByIdDescEmailAsc(String name); // Id 내림차순, Email 오름차순 정렬
    List<User> findByName(String name, Sort sort); // 파라미터로 Sort 조건을 받을 수 있도록 정의할 수도 있 (가독성을 높일 수 있음)

    // 페이징 처리
    Page<User> findByName(String name, Pageable pageable); // Page는 응답값, Pageable은 요청값
}

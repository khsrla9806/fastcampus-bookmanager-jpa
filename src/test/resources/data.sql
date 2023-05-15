call next value for hibernate_sequence; -- 이 설정을 해줘야 DB에 Sequence의 증가를 알려줄 수 있다.
insert into user (`id`, `name`, `email`, `created_at`, `updated_at`) values (1, 'hoon', 'hoon@naver.com', now(), now());

call next value for hibernate_sequence;
insert into user (`id`, `name`, `email`, `created_at`, `updated_at`) values (2, 'park', 'park@naver.com', now(), now());

call next value for hibernate_sequence;
insert into user (`id`, `name`, `email`, `created_at`, `updated_at`) values (3, 'min', 'min@naver.com', now(), now());

call next value for hibernate_sequence;
insert into user (`id`, `name`, `email`, `created_at`, `updated_at`) values (4, 'yoon', 'yoon@naver.com', now(), now());

call next value for hibernate_sequence;
insert into user (`id`, `name`, `email`, `created_at`, `updated_at`) values (5, 'hoon', 'hoon@kakao.com', now(), now());

package com.fastcampus.jpa.bookmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
    Embedded Class 생성
 */

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String city; // 시

    private String district; // 구

    private String detail; // 상세 주소

    private String zipCode; // 우편번호
}

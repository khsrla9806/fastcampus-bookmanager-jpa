package com.fastcampus.jpa.bookmanager.domain.converter;

import com.fastcampus.jpa.bookmanager.repository.dto.BookStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/*
    [ autoApply 사용 시 주의 ]
    별도의 커스텀 클래스를 생성한 경우에만 autoApply를 사용해야 한다.
    Long, String 등에 autoApply를 적용하게 되면, 해당 데이터 타입이 들어오면 모두 Conveter를 타기 때문에 의도치 않은 결과가 발생될 수 있다.
 */
@Converter(autoApply = true)
public class BookStatusConverter implements AttributeConverter<BookStatus, Integer> {

    /**
     * Java -> DB
     */
    @Override
    public Integer convertToDatabaseColumn(BookStatus attribute) {
        return attribute.getCode();
    }

    /**
     * DB -> Java
     */
    @Override
    public BookStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? new BookStatus(dbData) : null;
    }
}

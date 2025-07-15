package com.mins.splearn.domain;

import java.util.regex.Pattern;

public record Email(String address) {
    private static final Pattern EMAIN_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email {
        if (!EMAIN_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("이메일 형식이 바르지 않습니다." + address);
        }
    }

    /*
    갑 객체(Value Object)
    - 도메인 모델에서 식별자가 필요하지 않고 속성/값으로만 구별되는 오브젝트
    - 엔티티가 너무 많은 책임을 가지는 것을 방지하고
        특정 속성 관련 행위를 분리해서 엔티티를 더 집중된 상태로 유지하게 한다
    - 원시 타입보다는 도메인 개념을 더 명시적으로 나타내서 모델의 명확성을 높인다
    - 생성 이후에 상태가 변하지 않고 변경이 필요하면 새로운 객체로 교체한다
    - 풍부한 기능을 가진다
    - 자체 유효성 검사도 가능하다
     */

    /*
    원시 타입을 객체로 교체 - 리팩터링
    - 원시 타입의 남용을 개선하고 코드의 명시성과 안정성을 높이는 리팩터링 방법
    - 값 객체는 도메인 모델의 핵심 구성 요소로 값 그 자체의 의미를 담아내고 불변성, 동등성 등의 도메인 로직을 포함하여 설계하는 방식
     */
}

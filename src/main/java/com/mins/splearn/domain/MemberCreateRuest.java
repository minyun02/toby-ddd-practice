package com.mins.splearn.domain;

public record MemberCreateRuest(String email, String nickname, String password) {
//    검증이 필요하면 canonical constructor를 만들어서 그 안에서 검증한다.
}

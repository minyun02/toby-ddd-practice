package com.mins.splearn.application.provided;


import com.mins.splearn.domain.Member;
import com.mins.splearn.domain.MemberRegisterRequest;

/*
    회원 등록과 관련된 기능을 제공한다
 */
public interface MemberRegister {
    Member register(MemberRegisterRequest  memberRegisterRequest);
}

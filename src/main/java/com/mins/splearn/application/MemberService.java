package com.mins.splearn.application;

import com.mins.splearn.application.provided.MemberRegister;
import com.mins.splearn.application.required.EmailSender;
import com.mins.splearn.application.required.MemberRepository;
import com.mins.splearn.domain.Member;
import com.mins.splearn.domain.MemberRegisterRequest;
import com.mins.splearn.domain.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, EmailSender emailSender, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        // check
        // domain model
        Member member = Member.register(registerRequest, passwordEncoder);

        // repository
        memberRepository.save(member);

        // post process
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클륵해서 등록을 완료해주세요.");

        return member;
    }
}

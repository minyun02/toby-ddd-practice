package com.mins.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };

        member = Member.register(new MemberRegisterRequest("min@splearn.com", "min", "secret"), passwordEncoder);
    }

    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void activate() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        member.activate();

        assertThatThrownBy(() -> member.activate()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFail() {
        assertThatThrownBy(() -> member.deactivate())
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(() -> member.deactivate())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("min");

        member.changeNickname("MIN");

        assertThat(member.getNickname()).isEqualTo("MIN");
    }

    @Test
    void changePassword() {
        member.changePassword("hello",  passwordEncoder);

        assertThat(member.verifyPassword("hello", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        // 도메인의 정보를 이용해서 도메인과 관련된 또 다른 모델의 끌어와서 로직을 구현
        // 이렇게 밖에 나와있는건 응집도가 좋지 않다
        // if (member.getStatus() == MemberStatus.ACTIVE) {}
        // 도메인의 변경이 한 곳으로 집중될 수 있도록 만들어보자

        // member.isActive();
        // 값을 꺼내서 디스플레이 하거나 다른 용도로 사용하는 건 getter를 통해서 해도 되지만
        // 상태를 꺼내는 건 보통 로직을 적용하는 경우가 많아서 메서드로 만들어주자.
        // 그럼 isDeactive(), isPending()도 필요한거 아냐?
        // isActive()는 개발 초기에도 많이 사용될거같고 나머지는 필요할지 두고 봐야한다
        // 그 때 가서 만들어도 충분하다. -> 개인 + 경험적 판단

        assertThat(member.isActive()).isFalse();

        member.activate();
        assertThat(member.isActive()).isTrue();

        member.deactivate();
        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail() {
        assertThatThrownBy(() ->Member.register(new MemberRegisterRequest("invalid", "min", "secret"), passwordEncoder)).isInstanceOf(IllegalArgumentException.class);

        Member.register(new MemberRegisterRequest("www5981@naver.com", "min", "secret"), passwordEncoder);
    }
}
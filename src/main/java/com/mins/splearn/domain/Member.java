package com.mins.splearn.domain;

import lombok.Getter;
import lombok.ToString;

import static org.springframework.util.Assert.state;

@Getter
@ToString
public class Member {
    private String email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    public Member() {}

    private Member(String email, String nickname, String passwordHash) {
        this.email = email;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.status = MemberStatus.PENDING;
    }

    /*
    1. 정적 팩토리 메서드는 이름을 부여할 수 있다는 장점이 있다.
    2. 메서드 내부에서 부가적인 작업을 통해서 새로운 오브젝트를 만들지 않고 캐시에서 꺼내온다거나
    상속 구조를 통해서 서브 클래스 중에 하나의 인스턴스를 리턴한다거나 하는 기능을 수행
    3. 외부에 도메인 서비스 같은 걸 주입하는 기능이 필요할 때 사용
     */
    public static Member create(String email, String nickname, String passwordHash, PasswordEncoder passwordEncoder) {
        return new  Member(email, nickname, passwordEncoder.encode(passwordHash));
    }

    public void activate() {
//        if (MemberStatus.PENDING.equals(status)) {
//            throw new IllegalAccessException("PENDING 상태가 아닙니다");
//        }
        // 스프링프레임워크에서 제공해주는 간결한 검증 메서드
        // 에러 메시지는 개발자가 볼 에러라서 이 정도 수준으로 작성함
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this. passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String password,  PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(password);
    }
}

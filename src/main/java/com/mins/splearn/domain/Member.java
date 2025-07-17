package com.mins.splearn.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache //JPA first-level cache, second-level cache 찾아보기
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @NaturalId
    private Email email;

    private String nickname;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    /*
    1. 정적 팩토리 메서드는 이름을 부여할 수 있다는 장점이 있다.
    2. 메서드 내부에서 부가적인 작업을 통해서 새로운 오브젝트를 만들지 않고 캐시에서 꺼내온다거나
    상속 구조를 통해서 서브 클래스 중에 하나의 인스턴스를 리턴한다거나 하는 기능을 수행
    3. 외부에 도메인 서비스 같은 걸 주입하는 기능이 필요할 때 사용
     */
//    public static Member create(String email, String nickname, String passwordHash, PasswordEncoder passwordEncoder) {
    public static Member register(MemberRegisterRequest registerRuest, PasswordEncoder passwordEncoder) {
        /*
        파라미터가 String이 여러개 있는 상황에서 이 정적 팩토리 메서드를 호출하는 쪽에서는 파라미터의 순서를 바꿔서 넣은 경우가 많을거다.
        이런 경우, Builder 패턴을 사용해서 해결할 수 있지만 한계도 있다.
        Builder를 사용하면 어떤 파라미터에 뭐가 들어가는지 쉽게 눈으로 확인이 가능하지만,
        위 예시에서 passwordHash를 안넣어도 된다. passwordHash에 null이 들어갈뿐
        하지만 실행할때 에러가 발생할거다.
        그럼 어떻게 해결해야할까?
        파리미터 오브젝트
        - 파라미터의 개수가 너무 많을 때 사용할 수 있는 오브젝트
        - 생성하기 위해 전달되어지는 필수 회원 정보 항목을 묶어서 전달하는 방식
        - 파라미터가 추가되어도 memberCreateRequest만 수정하면 된다.
        - 아래를 보면 어떤 파라미터에 어떤 변수가 들어가는지 쉽게 보이니까 코드 리뷰 측면에서도 좋다
        * */

        Member member = new Member();

        member.email = new Email(registerRuest.email());
        member.nickname = requireNonNull(registerRuest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(registerRuest.password()));

        member.status = MemberStatus.PENDING;

        return member;
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
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String password,  PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}

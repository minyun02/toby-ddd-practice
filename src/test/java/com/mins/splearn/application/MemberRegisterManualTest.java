package com.mins.splearn.application;

import com.mins.splearn.application.provided.MemberRegister;
import com.mins.splearn.application.required.EmailSender;
import com.mins.splearn.application.required.MemberRepository;
import com.mins.splearn.domain.Email;
import com.mins.splearn.domain.Member;
import com.mins.splearn.domain.MemberFixture;
import com.mins.splearn.domain.MemberStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MemberRegisterManualTest {
    @Test
    void registerTestStub() {
        MemberRegister register = new MemberService(
                new MemberRepositoryStub(),
                new EmailSenderStub(),
                MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestMock() {
        EmailSenderMock emailSenderMock = new EmailSenderMock();

        MemberRegister register = new MemberService(
                new MemberRepositoryStub(),
                emailSenderMock,
                MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSenderMock.getTos()).hasSize(1);
        assertThat(emailSenderMock.getTos().get(0)).isEqualTo(member.getEmail());
    }

    @Test
    void registerTestMockito() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);

        MemberRegister register = new MemberService(
                new MemberRepositoryStub(),
                emailSenderMock,
                MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }
    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {
        }
    }

    static class EmailSenderMock implements EmailSender {
        /*
        실제 이 오브젝트하고 테스트가 수행되는 동안 어떤 상호작용을 했는지까지 검증하는 그런 기능을 제공해야 한다
        예: 호출 했나 안했나, 몇 번 호출 했나, 호출 할 때 어떤 파라미터가 넘어 왔나
         */

        List<Email> tos = new ArrayList<>();

        @Override
        public void send(Email email, String subject, String body) {
            tos.add(email);
        }

        public List<Email> getTos() {
            return tos;
        }
    }
}
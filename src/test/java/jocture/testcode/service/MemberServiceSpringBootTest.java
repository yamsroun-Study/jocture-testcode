package jocture.testcode.service;

import jocture.testcode.domain.Member;
import jocture.testcode.exception.DuplicateEmailMemberException;
import jocture.testcode.exception.NoExistsMemberException;
import jocture.testcode.repository.MemberRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest //스프링부트 테스트 or 통합테스트(API 테스트, End-to-End(E2E) 테스트)
@Transactional
class MemberServiceSpringBootTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    //TC-Test Case
    //@Disabled
    @Test
    //@RepeatedTest(10)
    //@Commit
    //@Rollback
    @DisplayName("회원 가입 - 정상")
    void join() {
        //BDD 스타일 - Given/When/Then
        //given
        Member member = new Member("jjlim", "jjlim@ab.com");

        //when
        memberService.join(member);

        //then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getId()).isPositive();

        Optional<Member> result = memberRepository.findById(member.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(member.getName());
        assertThat(result.get().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 가입 - 이메일 중복")
    void join_duplicateEmail() {
        //given
        Member member1 = new Member("jjlim", "jjlim@ab.com");
        memberRepository.save(member1);
        Member member2 = new Member("jjlim22222", "jjlim@ab.com");

        //when
        ThrowableAssert.ThrowingCallable callable = () -> memberService.join(member2);

        //then
        assertThatThrownBy(callable).isInstanceOf(DuplicateEmailMemberException.class);
    }

    @Test
    @DisplayName("회원 조회 - 결과 있음")
    void getMember() {
        //given
        Member member = new Member("jjlim", "jjlim@ab.com");
        memberRepository.save(member);

        //when
        Member result = memberService.getMember(member.getId());

        //then
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 조회 - 결과 없음")
    void getMember_noExists() {
        //given
        int noExistsMemberId = -999;

        //when
        ThrowableAssert.ThrowingCallable callable = () -> memberService.getMember(noExistsMemberId);

        //then
        assertThatThrownBy(callable).isInstanceOf(NoExistsMemberException.class);
    }
}

package jocture.testcode.service;

import jocture.testcode.domain.Member;
import jocture.testcode.exception.DuplicateEmailMemberException;
import jocture.testcode.exception.NoExistsMemberException;
import jocture.testcode.repository.MemberRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입 - 정상")
    void join() {
        //given
        Member member = new Member("jjlim", "jjlim@ab.com");

        //when
        memberService.join(member);

        //then
        then(memberRepository).should().save(member);
        //then(memberRepository).should(times(1)).save(member);
        //then(memberRepository).should(atLeastOnce()).save(member);
    }

    @Test
    @DisplayName("회원 가입 - 이메일 중복")
    void join_duplicateEmail() {
        //given
        Member member1 = new Member("jjlim", "jjlim@ab.com");
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member1));

        //when
        Member member = new Member("jjlim", "jjlim@ab.com");
        ThrowableAssert.ThrowingCallable callable = () -> memberService.join(member);

        //then
        assertThatThrownBy(callable).isInstanceOf(DuplicateEmailMemberException.class);
        then(memberRepository).should(never()).save(member);
    }

    @Test
    @DisplayName("회원 조회 - 결과 있음")
    void getMember() {
        //given
        Member member = new Member("jjlim", "jjlim@ac.com");
        given(memberRepository.findById(anyInt())).willReturn(Optional.of(member));

        //when
        int memberId = 1;
        Member result = memberService.getMember(memberId);

        //then
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 조회 - 결과 없음")
    void getMember_noExists() {
        //given
        given(memberRepository.findById(anyInt())).willReturn(Optional.empty());

        //when
        int noExistsMemberId = -999;
        ThrowableAssert.ThrowingCallable callable = () -> memberService.getMember(noExistsMemberId);

        //then
        assertThatThrownBy(callable).isInstanceOf(NoExistsMemberException.class);
    }
}

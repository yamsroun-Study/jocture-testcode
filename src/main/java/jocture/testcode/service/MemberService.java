package jocture.testcode.service;

import jocture.testcode.domain.Member;
import jocture.testcode.exception.DuplicateEmailMemberException;
import jocture.testcode.exception.NoExistsMemberException;
import jocture.testcode.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void join(Member member) {
        validateForJoin(member);
        memberRepository.save(member);
    }

    private void validateForJoin(Member member) {
        Optional<Member> existsMember = memberRepository.findByEmail(member.getEmail());
        if (existsMember.isPresent()) {
            throw new DuplicateEmailMemberException("이미 등록된 이메일입니다.");
        }
    }

    public Member getMember(int id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NoExistsMemberException("회원 정보가 없습니다."));
    }
}

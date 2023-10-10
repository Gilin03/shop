package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void updateMember(Member member) {
        Member existingMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        // 이메일이 변경되었는지 확인하고, 변경되었다면 중복 체크를 수행합니다.
        if(!existingMember.getEmail().equals(member.getEmail())) {
            validateDuplicateMember(member);
        }

        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        existingMember.setAddress(member.getAddress());

        // 비밀번호가 널이 아니고 비어 있지 않다면 업데이트합니다.
        if (member.getPassword() != null && !member.getPassword().isEmpty()) {
            existingMember.setPassword(member.getPassword());
        }

        // 저장하지 않아도, JPA가 트랜잭션 종료 시점에 변경 감지 (Dirty Checking)를 하여 업데이트 쿼리를 실행합니다.
        // 만약 다른 로직이 추가로 필요하다면 memberRepository.save(existingMember);를 호출하세요.
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElse(null); // 회원이 없을 경우 null을 반환하거나 예외 처리를 수행하세요.
    }



    public void deleteMember(Long id) {
        Member memberToDelete = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        memberRepository.delete(memberToDelete);
    }
}
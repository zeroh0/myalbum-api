package com.myalbum.member.service;

import com.myalbum.member.controller.dto.SignUpResponse;
import com.myalbum.member.entity.Member;
import com.myalbum.member.repository.MemberRepository;
import com.myalbum.member.service.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     *
     * @param signUpDto 회원 가입 정보
     * @return 회원 가입 응답
     */
    public SignUpResponse signUp(final SignUpDto signUpDto) {
        // 이메일 중복 검사
        if (memberRepository.existsByEmail(signUpDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 사용자명 중복 검사
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }

        // 회원 정보 저장
        Member savedMember = memberRepository.save(
                Member.builder()
                        .email(signUpDto.getEmail())
                        .password(signUpDto.getPassword())
                        .username(signUpDto.getUsername())
                        .build()
        );

        return SignUpResponse.fromEntity(savedMember);
    }

}

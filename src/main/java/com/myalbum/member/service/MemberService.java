package com.myalbum.member.service;

import com.myalbum.member.controller.dto.SignUpResponse;
import com.myalbum.member.entity.Member;
import com.myalbum.member.enums.MemberStatus;
import com.myalbum.member.repository.MemberRepository;
import com.myalbum.member.service.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * 일반 회원 가입
     *
     * @param signUpDto 회원 가입 정보
     * @return 회원 가입 응답
     */
    public SignUpResponse signUp(final SignUpDto signUpDto) {
        // 일반회원 이메일 중복 검사
        if (memberRepository.existsByEmailAndProviderIsNull(signUpDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 사용자명 중복 검사
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }

        // 회원 정보 저장
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Member savedMember = memberRepository.save(
                Member.builder()
                        .email(signUpDto.getEmail())
                        .password(encodedPassword)
                        .username(signUpDto.getUsername())
                        .status(MemberStatus.ACTIVE.getCode())
                        .build()
        );

        return SignUpResponse.fromEntity(savedMember);
    }

}

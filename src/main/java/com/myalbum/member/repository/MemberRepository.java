package com.myalbum.member.repository;

import com.myalbum.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일 중복 여부 확인 (일반 회원)
     *
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmailAndProviderIsNull(String email);

    /**
     * 사용자핸들 존재 여부 확인
     *
     * @param username 사용자핸들
     * @return 존재 여부
     */
    boolean existsByUsername(String username);


    /**
     * 일반 로그인 회원 조회
     *
     * @param email 이메일
     * @return 회원
     */
    Optional<Member> findByEmailAndProviderIsNull(String email);

    /**
     * 소셜 로그인 회원 조회
     *
     * @param provider   소셜 로그인 제공자
     * @param providerId 제공자 ID
     * @return 회원
     */
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

}

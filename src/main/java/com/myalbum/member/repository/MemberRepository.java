package com.myalbum.member.repository;

import com.myalbum.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일 존재 여부 확인
     *
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 사용자핸들 존재 여부 확인
     *
     * @param username 사용자핸들
     * @return 존재 여부
     */
    boolean existsByUsername(String username);

}

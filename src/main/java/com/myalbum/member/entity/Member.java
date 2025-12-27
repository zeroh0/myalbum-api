package com.myalbum.member.entity;

import com.myalbum.member.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 회원 엔티티
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(comment = "pk", nullable = false, columnDefinition = "BIGINT")
    private Long id;

    @Column(comment = "이메일", unique = true, nullable = false, columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(comment = "비밀번호", columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(comment = "사용자핸들", unique = true, columnDefinition = "VARCHAR(50)")
    private String username;

    @Column(comment = "화면표시명", columnDefinition = "VARCHAR(100)")
    private String displayName;

    @Column(comment = "프로필 이미지 URL", columnDefinition = "VARCHAR(500)")
    private String profileImageUrl;

    @Column(comment = "OAuth2 제공자", columnDefinition = "VARCHAR(20)")
    private String provider;

    @Column(comment = "OAuth2 제공자 ID", columnDefinition = "VARCHAR(100)")
    private String providerId;

    @Column(comment = "회원상태", nullable = false, columnDefinition = "VARCHAR(20)")
    private String status;

    @CreationTimestamp
    @Column(comment = "생성일시", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(comment = "수정일시", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 회원 상태가 PENDING 인지 여부 반환
     *
     * @return 회원 상태가 PENDING 이면 true, 아니면 false
     */
    public boolean isPending() {
        return MemberStatus.PENDING.getCode().equalsIgnoreCase(this.status);
    }

}

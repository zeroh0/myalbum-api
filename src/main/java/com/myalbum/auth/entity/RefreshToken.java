package com.myalbum.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(comment = "pk", nullable = false, columnDefinition = "BIGINT")
    private Long id;

    @Column(comment = "회원 pk", nullable = false, columnDefinition = "BIGINT")
    private Long memberId;

    @Column(comment = "토큰", nullable = false, columnDefinition = "VARCHAR(100)")
    private String token;

    @Column(comment = "만료일시", nullable = false, columnDefinition = "BIGINT")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(comment = "생성일시", nullable = false, columnDefinition = "BIGINT")
    private LocalDateTime createdAt;

    /**
     * RefreshToken 생성
     *
     * @param memberId         회원 pk
     * @param token            토큰
     * @param expirationMillis 만료 시간 (밀리초)
     * @return RefreshToken
     */
    public static RefreshToken create(Long memberId, String token, long expirationMillis) {
        return RefreshToken.builder()
                .memberId(memberId)
                .token(token)
                .expiresAt(LocalDateTime.now().plusSeconds(expirationMillis / 1000))
                .build();
    }

    /**
     * 토큰 만료 여부 확인
     *
     * @return 만료되었으면 true, 아니면 false
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

}

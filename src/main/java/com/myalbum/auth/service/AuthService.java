package com.myalbum.auth.service;

import com.myalbum.auth.controller.dto.JwtToken;
import com.myalbum.auth.entity.RefreshToken;
import com.myalbum.auth.exception.AuthError;
import com.myalbum.auth.repository.RefreshTokenRepository;
import com.myalbum.common.error.exception.AppException;
import com.myalbum.config.security.jwt.JwtTokenProvider;
import com.myalbum.member.entity.Member;
import com.myalbum.member.exception.MemberError;
import com.myalbum.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 토큰 생성
     *
     * @param memberId 회원 pk
     * @return TokenResponse
     */
    public JwtToken generateToken(final Long memberId) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);

        // 기존 Refresh Token 삭제 후 새로 저장 (Refresh Token Rotation)
        refreshTokenRepository.deleteByMemberId(memberId);
        refreshTokenRepository.save(
                RefreshToken.create(
                        memberId,
                        refreshToken,
                        jwtTokenProvider.getRefreshTokenExpiration()
                )
        );

        return JwtToken.of(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpiration()
        );
    }

    /**
     * Refresh Token으로 새로운 Access Token과 Refresh Token 발급
     *
     * @param refreshToken 기존 Refresh Token
     * @return 새로운 JwtToken
     */
    public JwtToken refreshToken(final String refreshToken) {
        // Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            AppException.exception(AuthError.INVALID_REFRESH_TOKEN);
        }

        // FIXME: 조회 성능 개선 필요
        // Refresh Token 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> AppException.exception(AuthError.REFRESH_TOKEN_NOT_FOUND));

        // 만료 여부 확인
        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            AppException.exception(AuthError.REFRESH_TOKEN_EXPIRED);
        }

        // 회원 조회
        Member member = memberRepository.findById(storedToken.getMemberId())
                .orElseThrow(() -> AppException.exception(MemberError.MEMBER_NOT_FOUND));

        // 새로운 토큰 발급 (Refresh Token Rotation)
        return generateToken(member.getId());
    }

    /**
     * 로그아웃 처리 (Refresh Token 삭제)
     *
     * @param memberId 회원 pk
     */
    public void logout(final Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }

}

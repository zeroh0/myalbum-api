package com.myalbum.auth.controller;

import com.myalbum.auth.controller.dto.JwtToken;
import com.myalbum.auth.controller.dto.TokenResponse;
import com.myalbum.auth.service.AuthService;
import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.security.cookie.RefreshTokenCookieManager;
import com.myalbum.config.security.domain.PrincipalDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    /**
     * Token 갱신
     *
     * @param refreshToken Refresh Token
     * @return 새로운 Access Token 및 Refresh Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        // Refresh Token으로부터 새로운 토큰 발급
        JwtToken jwtToken = authService.refreshToken(refreshToken);

        // 응답용 토큰 정보 생성
        TokenResponse tokenResponse = new TokenResponse(jwtToken.getAccessToken(), jwtToken.getAccessTokenExpiresIn(), jwtToken.getTokenType());

        // Refresh Token을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = refreshTokenCookieManager.createRefreshTokenCookie(jwtToken.getRefreshToken());
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ApiResponse.ok(tokenResponse);
    }

    /**
     * 로그아웃 (Refresh Token 폐기)
     *
     * @param principalDetails 현재 인증된 사용자
     * @return 성공 응답
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        authService.logout(principalDetails.getMember().getId());

        return ApiResponse.ok();
    }

}

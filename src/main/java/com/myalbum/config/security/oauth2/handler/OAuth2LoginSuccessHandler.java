package com.myalbum.config.security.oauth2.handler;

import com.myalbum.auth.controller.dto.JwtToken;
import com.myalbum.auth.service.AuthService;
import com.myalbum.config.security.cookie.RefreshTokenCookieManager;
import com.myalbum.config.security.domain.PrincipalDetails;
import com.myalbum.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.host}")
    private String appHost;

    private final AuthService authService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        Member member = ((PrincipalDetails) authentication.getPrincipal()).getMember();

        // 토큰 발급
        JwtToken jwtToken = authService.generateToken(member.getId());

        // Refresh Token을 HttpOnly 쿠키로 설정 (PENDING, ACTIVE 공통)
        ResponseCookie refreshTokenCookie = refreshTokenCookieManager.createRefreshTokenCookie(jwtToken.getRefreshToken());
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());

        // PENDING 회원(추가 정보 미입력)만 온보딩 페이지로, 그 외에는 홈으로 리다이렉트
        String redirectPath = member.isPending() ? "/signup/onboarding" : "/";
        String redirectUrl = UriComponentsBuilder
                .fromUriString(appHost + redirectPath)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}

package com.myalbum.config.security.oauth2.handler;

import com.myalbum.auth.controller.dto.TokenResponse;
import com.myalbum.auth.service.AuthService;
import com.myalbum.config.security.domain.PrincipalDetails;
import com.myalbum.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        Member member = ((PrincipalDetails) authentication.getPrincipal()).getMember();

        // 토큰 발급
        TokenResponse tokenResponse = authService.generateToken(member.getId());

        if (member.isPending()) {
            // PENDING 상태의 회원인 경우, 추가 정보 입력 페이지로 리다이렉트
            String redirectUrl = UriComponentsBuilder
                    .fromUriString(appHost + "/signup/onboarding")
                    .queryParam("accessToken", tokenResponse.getAccessToken())
                    .queryParam("refreshToken", tokenResponse.getRefreshToken())
                    .build()
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

}

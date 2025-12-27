package com.myalbum.config.security.oauth2.handler;

import com.myalbum.config.security.domain.PrincipalDetails;
import com.myalbum.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();

        if (member.isPending()) {
            // PENDING 상태의 회원인 경우, 추가 정보 입력 페이지로 리다이렉트
            getRedirectStrategy().sendRedirect(request, response, "/");
        } else {
            // 정상 회원인 경우, 기본 리다이렉트 처리
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

}

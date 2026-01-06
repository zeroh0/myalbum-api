package com.myalbum.config.security.handler;

import com.myalbum.auth.exception.AuthError;
import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.message.MessageResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 인증된 사용자가 권한이 없는 리소스에 접근하려고 하는 경우 처리 (403 Forbidden)
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        ApiResponse<Void> responseBody = new ApiResponse<>(
                false,
                HttpStatus.FORBIDDEN.value(),
                MessageResolver.getMessage(AuthError.UNAUTHORIZED),
                null
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

}

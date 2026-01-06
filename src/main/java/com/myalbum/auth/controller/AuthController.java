package com.myalbum.auth.controller;

import com.myalbum.auth.controller.dto.TokenRefreshRequest;
import com.myalbum.auth.controller.dto.TokenResponse;
import com.myalbum.auth.service.AuthService;
import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.security.domain.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Token 갱신
     *
     * @param request Refresh Token
     * @return 새로운 Access Token 및 Refresh Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody final TokenRefreshRequest request
    ) {
        String refreshToken = request.getRefreshToken();

        return ApiResponse.ok(authService.refreshToken(refreshToken));
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

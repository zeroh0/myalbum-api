package com.myalbum.member.controller;

import com.myalbum.auth.controller.dto.JwtToken;
import com.myalbum.auth.controller.dto.LoginMember;
import com.myalbum.auth.controller.dto.LoginResponse;
import com.myalbum.auth.controller.dto.TokenResponse;
import com.myalbum.auth.service.AuthService;
import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.security.cookie.RefreshTokenCookieManager;
import com.myalbum.config.security.domain.PrincipalDetails;
import com.myalbum.member.controller.dto.LoginRequest;
import com.myalbum.member.controller.dto.SignUpRequest;
import com.myalbum.member.controller.dto.SignUpResponse;
import com.myalbum.member.entity.Member;
import com.myalbum.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;
    private final AuthenticationManager authenticationManager;

    /**
     * 회원 가입
     *
     * @param signUpRequest 회원 가입 요청
     * @return 회원 가입 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        return ApiResponse.ok(memberService.signUp(signUpRequest.toServiceDto()));
    }

    /**
     * 로그인
     *
     * @param loginRequest 로그인 요청
     * @return JWT 토큰 응답
     */
    @PostMapping("/login")
    @Transactional
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody final LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // 인증 수행
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 인증된 회원 정보 가져오기
        Member member = ((PrincipalDetails) authentication.getPrincipal()).getMember();

        // JWT 토큰 생성
        JwtToken jwtToken = authService.generateToken(member.getId());

        // 응답용 토큰 정보 생성
        TokenResponse tokenResponse = new TokenResponse(jwtToken.getAccessToken(), jwtToken.getAccessTokenExpiresIn(), jwtToken.getTokenType());

        // Refresh Token을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = refreshTokenCookieManager.createRefreshTokenCookie(jwtToken.getRefreshToken());
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ApiResponse.ok(new LoginResponse(tokenResponse, LoginMember.fromEntity(member)));
    }

    /**
     * PENDING 상태 회원 추가입력사항 작업
     *
     * @param principalDetails
     * @param memberOnboardingRequest
     * @return
     */
    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponse<Void>> onboarding(
            @AuthenticationPrincipal final PrincipalDetails principalDetails,
            @Valid @RequestBody final MemberOnboardingRequest memberOnboardingRequest
    ) {
        Member member = principalDetails.getMember();

        // PENDING 상태의 회원 username 설정 및 상태를 ACTIVE로 변경
        memberService.completeOnboarding(member.getId(), memberOnboardingRequest.getUsername());

        return ApiResponse.ok();
    }

    /**
     * 현재 로그인한 회원 정보 조회
     *
     * @param principalDetails 인증된 회원 정보
     * @return 현재 로그인한 회원 정보
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LoginMember>> getCurrentMember(
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Member loggedInMember = principalDetails.getMember();
        Member member = memberService.findById(loggedInMember.getId());

        return ApiResponse.ok(LoginMember.fromEntity(member));
    }

}

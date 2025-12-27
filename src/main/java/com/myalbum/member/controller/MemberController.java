package com.myalbum.member.controller;

import com.myalbum.member.controller.dto.LoginRequest;
import com.myalbum.member.controller.dto.SignUpRequest;
import com.myalbum.member.controller.dto.SignUpResponse;
import com.myalbum.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    /**
     * 회원 가입
     *
     * @param signUpRequest 회원 가입 요청
     * @return 회원 가입 응답
     */
    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody final SignUpRequest signUpRequest) {
        return memberService.signUp(signUpRequest.toServiceDto());
    }

    /**
     * 로그인
     *
     * @param loginRequest 로그인 요청
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public String login(
            @RequestBody final LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // 인증 수행
        Authentication authenticate = authenticationManager.authenticate(authToken);

        // SecurityContext에 인증 정보 저장
        SecurityContext securityContext = securityContextHolderStrategy.getContext();
        securityContext.setAuthentication(authenticate);
        securityContextHolderStrategy.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);

        return "로그인 성공";
    }

}

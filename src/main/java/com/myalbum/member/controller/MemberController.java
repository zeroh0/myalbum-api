package com.myalbum.member.controller;

import com.myalbum.member.controller.dto.SignUpRequest;
import com.myalbum.member.controller.dto.SignUpResponse;
import com.myalbum.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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

}

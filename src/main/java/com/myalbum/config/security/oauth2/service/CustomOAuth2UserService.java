package com.myalbum.config.security.oauth2.service;

import com.myalbum.config.security.domain.PrincipalDetails;
import com.myalbum.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.myalbum.config.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.myalbum.member.entity.Member;
import com.myalbum.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    /**
     * 소셜 로그인 사용자 정보 로드
     *
     * @param userRequest 제공받은 사용자 요청 정보
     * @return 소셜 로그인 사용자 정보
     * @throws OAuth2AuthenticationException
     */
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 소셜 로그인 타입
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 사용자 정보 추출
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        // 소셜 로그인 회원 조회 -> PENDING 상태의 임시 회원 생성
        Member member = memberRepository.findByProviderAndProviderId(registrationId, oAuth2UserInfo.getProviderId())
                .orElseGet(() -> {
                    // 임시 회원 생성
                    return memberRepository.save(oAuth2UserInfo.toMemberEntity());
                });

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }

}



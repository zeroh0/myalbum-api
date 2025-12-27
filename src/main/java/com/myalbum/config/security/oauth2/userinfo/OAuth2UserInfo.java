package com.myalbum.config.security.oauth2.userinfo;

import com.myalbum.member.entity.Member;
import com.myalbum.member.enums.MemberStatus;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getProviderEmail();

    default Member toMemberEntity() {
        return Member.builder()
                .email(this.getProviderEmail())
                .provider(this.getProvider())
                .providerId(this.getProviderId())
                .status(MemberStatus.PENDING.getCode())
                .build();
    }

}

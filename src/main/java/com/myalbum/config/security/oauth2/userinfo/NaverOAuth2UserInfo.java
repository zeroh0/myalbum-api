package com.myalbum.config.security.oauth2.userinfo;

import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return String.valueOf(response.get("id"));
    }

    @Override
    public String getProviderEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return String.valueOf(response.get("email"));
    }

}

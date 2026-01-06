package com.myalbum.member.controller;

import com.myalbum.common.valid.RequiredField;
import lombok.Getter;

@Getter
public class MemberOnboardingRequest {

    @RequiredField(message = "input.please.enter", params = "member.field.username")
    private String username;

}

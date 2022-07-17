package com.szs.member.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessEnum {

    LOGIN(HttpStatus.OK.value(),  "로그인"),
    SIGN_UP(HttpStatus.OK.value(),  "회원가입"),
    MY_INFO(HttpStatus.OK.value(),  "내정보"),
    SCRAP(HttpStatus.OK.value(),  "동기스크랩"),
    REFUND(HttpStatus.OK.value(), "환급액");
    private final int code;
    private String message;

    SuccessEnum(int code, String message) {
        this.code = code;
        this.message = message;

    }

}

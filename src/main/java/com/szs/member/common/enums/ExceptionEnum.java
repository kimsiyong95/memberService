package com.szs.member.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    ID_OR_PASS_ERROR(HttpStatus.BAD_REQUEST, "E001", "아이디 및 비밀번호 오류"),
    USER_PARAM_ERROR(HttpStatus.BAD_REQUEST, "E002", "사용자 파라미터 오류"),
    
    USER_DUPLI_ERROR(HttpStatus.BAD_REQUEST, "E003", "이미 가입된 사용자"),

    TOKEN_ERROR(HttpStatus.BAD_REQUEST, "E004", "토큰 오류"),
    
    NON_AUTHENTICATION_ERROR(HttpStatus.BAD_REQUEST, "E005", "인증되지 않은 사용자");

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }
    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

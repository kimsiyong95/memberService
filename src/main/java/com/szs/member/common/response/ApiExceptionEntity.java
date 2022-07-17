package com.szs.member.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiExceptionEntity {
    private String errorCdoe;
    private String errorMessage;

    @Builder
    public ApiExceptionEntity(HttpStatus status, String errorCdoe, String errorMessage) {
        this.errorCdoe = errorCdoe;
        this.errorMessage =errorMessage;
    }
}

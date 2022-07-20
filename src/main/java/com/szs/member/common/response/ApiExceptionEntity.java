package com.szs.member.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiExceptionEntity {

    @Schema(description = "에러코드", defaultValue = "")
    private String errorCode;

    @Schema(description = "에러메세지", defaultValue = "")
    private String errorMessage;

    @Builder
    public ApiExceptionEntity(HttpStatus status, String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage =errorMessage;
    }
}

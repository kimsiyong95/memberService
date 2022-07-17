package com.szs.member.common.response;

import com.szs.member.common.enums.ExceptionEnum;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private ExceptionEnum error;

    public ApiException(ExceptionEnum e){
        super(e.getMessage());
        this.error = e;
    }
}

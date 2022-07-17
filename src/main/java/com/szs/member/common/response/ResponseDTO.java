package com.szs.member.common.response;


import com.szs.member.common.enums.SuccessEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private int status;
    private String message;
    private T data;

    public ResponseDTO createResponseDTO(SuccessEnum successEnum, T data){
        this.status = successEnum.getCode();
        this.message = successEnum.getMessage();
        this.data = data;
        return this;
    }
}

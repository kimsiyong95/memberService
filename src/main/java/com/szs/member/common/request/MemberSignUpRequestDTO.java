package com.szs.member.common.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequestDTO {

    @ApiModelProperty(value = "아이디", dataType = "string", required = true, example = "kimsiyong")
    @NotEmpty
    private String userId;

    @ApiModelProperty(value = "비밀번호", dataType = "string", required = true, example = "1234")
    @NotEmpty
    private String password;

    @ApiModelProperty(value = "이름", dataType = "string", required = true, example = "홍길동")
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "주민번호", dataType = "string", required = true, example = "860824-1655068")
    @NotEmpty
    private String regNo;

    @ApiModelProperty(hidden = true)
    private String roles;

}

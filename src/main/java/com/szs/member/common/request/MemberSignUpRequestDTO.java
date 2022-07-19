package com.szs.member.common.request;


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

    @NotEmpty
    private String userId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    private String regNo;
    private String roles;

}

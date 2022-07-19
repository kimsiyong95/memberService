package com.szs.member.common.request;


import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequestDTO {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String password;


}

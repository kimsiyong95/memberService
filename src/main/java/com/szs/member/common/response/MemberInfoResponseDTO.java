package com.szs.member.common.response;

import com.szs.member.common.encryption.AES256;
import com.szs.member.common.enums.ExceptionEnum;
import com.szs.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDTO {

    private int userNo;
    private String userId;
    private String name;
    private String regNo;

    public static MemberInfoResponseDTO createMemberInfoDTO(Optional<Member> member) {
        if(!member.isPresent()){
            throw new ApiException(ExceptionEnum.NON_AUTHENTICATION_ERROR);
        }

        try {
            AES256 aes256 = new AES256();
            return MemberInfoResponseDTO.builder()
                    .userNo(member.get().getUserNo())
                    .userId(member.get().getUserId())
                    .name(member.get().getName())
                    .regNo(aes256.decrypt(member.get().getRegNo()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

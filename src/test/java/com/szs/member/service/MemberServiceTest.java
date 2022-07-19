package com.szs.member.service;

import com.szs.member.common.enums.ExceptionEnum;
import com.szs.member.common.request.MemberLoginRequestDTO;
import com.szs.member.common.request.MemberSignUpRequestDTO;
import com.szs.member.common.response.ApiException;
import com.szs.member.domain.Member;
import com.szs.member.jwt.JwtTokenProvider;
import com.szs.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;


    @DisplayName("중복 회원 가입 Exception Test")
    @Test
    public void siginUpDuplExceptionTest(){

        MemberSignUpRequestDTO memberSignUpRequestDTO = MemberSignUpRequestDTO.builder()
                        .userId("kimsiyong")
                        .name("김시용")
                        .regNo("123456-123456")
                        .password("1234")
                        .build();

        when(memberRepository.findTop1ByUserIdOrRegNo(any(), any())).thenReturn(createMember());

        ApiException error = assertThrows(ApiException.class, () -> {
            memberService.siginUp(memberSignUpRequestDTO);
        });


        assertThat(error.getMessage()).isEqualTo(ExceptionEnum.USER_DUPLI_ERROR.getMessage());
    }


    @DisplayName("로그인 Exception Test")
    @Test
    public void loginExceptionTest(){
        MemberLoginRequestDTO memberLoginRequestDTO = MemberLoginRequestDTO.builder()
                .userId("kimsiyong")
                .password("1234")
                .build();

        when(memberRepository.findByUserId(any())).thenReturn(createMember());

        ApiException error = assertThrows(ApiException.class, () -> {
            memberService.login(memberLoginRequestDTO);
        });


        assertThat(error.getMessage()).isEqualTo(ExceptionEnum.ID_OR_PASS_ERROR.getMessage());
    }

    @DisplayName("내 정보 조회 Exception Test")
    @Test
    public void getMyInfoExceptionTest(){
        when(memberRepository.findByUserId(any())).thenReturn(null);

        ApiException error = assertThrows(ApiException.class, () -> {
            memberService.getMyInfo("kimsiyong");
        });

        assertThat(error.getMessage()).isEqualTo(ExceptionEnum.NON_AUTHENTICATION_ERROR.getMessage());

    }


    public Member createMember(){
        return  Member.builder()
                      .userId("kimsiyong")
                      .name("김시용")
                      .regNo("123456-123456")
                      .build();
    }


}
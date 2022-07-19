package com.szs.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szs.member.common.encryption.AES256;
import com.szs.member.common.enums.SuccessEnum;
import com.szs.member.common.request.MemberLoginRequestDTO;
import com.szs.member.common.request.MemberSignUpRequestDTO;
import com.szs.member.common.response.MemberInfoResponseDTO;
import com.szs.member.common.response.ResponseDTO;
import com.szs.member.domain.Member;
import com.szs.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberRestController.class)
class MemberRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;




    @DisplayName("회원가입 Test")
    @Test
    @WithMockUser
    public void signUpTest() throws Exception {
        MemberSignUpRequestDTO memberSignUpRequestDTO = MemberSignUpRequestDTO.builder()
                .userId("kimsiyong")
                .name("김시용")
                .regNo("123456-123456")
                .password("1234")
                .build();


        mvc.perform(post("/api/v1/szs/signup")
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                        .andExpect(status().isCreated());
    }


    @DisplayName("로그인 실패 Test")
    @Test
    @WithMockUser
    public void loginExceptionTest() throws Exception {

        MemberLoginRequestDTO memberLoginRequestDTO = MemberLoginRequestDTO.builder()
                        .userId("kimsiyong")
                        .build();

        mvc.perform(post("/api/v1/szs/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                        .andExpect(status().isBadRequest());
    }

    @DisplayName("내 정보 조회 Test")
    @Test
    @WithMockUser
    public void myInfoTest() throws Exception {
        AES256 aes256 = new AES256();
        Member member = Member.builder()
                              .userId("kimsiyong")
                              .name("김시용")
                              .regNo(aes256.encrypt("123456-123456"))
                              .build();


        ResponseDTO responseDTO = new ResponseDTO()
                .createResponseDTO(SuccessEnum.MY_INFO, MemberInfoResponseDTO
                        .createMemberInfoDTO(Optional.ofNullable(member)));

        when(memberService.getMyInfo(any())).thenReturn(responseDTO);

        mvc.perform(get("/api/v1/szs/me"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("123456-123456")))
                .andExpect(content().string(containsString("kimsiyong")))
                .andExpect(content().string(containsString("내정보")));

    }

}
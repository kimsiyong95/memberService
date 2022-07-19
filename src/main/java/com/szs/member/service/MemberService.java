package com.szs.member.service;


import com.szs.member.common.enums.ExceptionEnum;
import com.szs.member.common.enums.SuccessEnum;
import com.szs.member.common.request.MemberLoginRequestDTO;
import com.szs.member.common.request.MemberSignUpRequestDTO;
import com.szs.member.common.response.ApiException;
import com.szs.member.common.response.MemberInfoResponseDTO;
import com.szs.member.common.response.ResponseDTO;
import com.szs.member.domain.Member;
import com.szs.member.jwt.JwtTokenProvider;
import com.szs.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public ResponseDTO siginUp(MemberSignUpRequestDTO memberRequestDTO) {
        Member member = Member.createMember(memberRequestDTO, passwordEncoder);

        validateDuplicated(member);

        memberRepository.save(member);
        return new ResponseDTO().createResponseDTO(SuccessEnum.SIGN_UP, new HashMap<>());
    }

    public void validateDuplicated(Member member) {
        if(Optional.ofNullable(memberRepository.findByUserIdOrRegNo(member.getUserId(),  member.getRegNo())).isPresent())
            throw new ApiException(ExceptionEnum.USER_DUPLI_ERROR);
    }

    @Transactional(readOnly = true)
    public ResponseDTO login(MemberLoginRequestDTO memberLoginRequestDTO){
        Optional<Member> member = Optional.ofNullable(memberRepository
                .findByUserId(memberLoginRequestDTO.getUserId()));

        if(!member.isPresent() || !passwordEncoder.matches(memberLoginRequestDTO.getPassword(), member.get().getPassword())){
            throw new ApiException(ExceptionEnum.ID_OR_PASS_ERROR);
        }


        return new ResponseDTO().createResponseDTO(SuccessEnum.LOGIN
                , jwtTokenProvider.createToken(member.get().getUserId()));
    }

    @Transactional(readOnly = true)
    public ResponseDTO getMyInfo(String userId){
        return new ResponseDTO().createResponseDTO(SuccessEnum.MY_INFO
                , MemberInfoResponseDTO.createMemberInfoDTO(
                        Optional.ofNullable(memberRepository.findByUserId(userId))));
    }


    @Transactional
    public ResponseDTO getScrap(String userId){
        Optional<Member> findMember = Optional.ofNullable(memberRepository.findByUserId(userId));

        if(!findMember.isPresent())
            throw new ApiException(ExceptionEnum.NON_AUTHENTICATION_ERROR);


        Map<String, Object> resultScrap= sendPost("https://codetest.3o3.co.kr/v1/scrap"
                , findMember.get().getScrapSendData());


        findMember.get().updateScrapData(resultScrap);

        return new ResponseDTO().createResponseDTO(SuccessEnum.SCRAP, resultScrap);
    }


    public ResponseDTO getMyRefund(String userId){
        Optional<Member> findMember = Optional.ofNullable(memberRepository.findByUserId(userId));

        if(!findMember.isPresent())
            throw new ApiException(ExceptionEnum.NON_AUTHENTICATION_ERROR);

        return new ResponseDTO().createResponseDTO(SuccessEnum.REFUND, findMember.get().getMyRefund());
    }


    public Map<String, Object> sendPost(String uri, Map<String, Object> param){
        return WebClient.builder()
                        .build()
                        .post()
                        .uri(uri)
                        .bodyValue(param)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                        .block();
    }
}

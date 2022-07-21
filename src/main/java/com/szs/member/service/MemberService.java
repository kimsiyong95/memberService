package com.szs.member.service;


import com.szs.member.common.enums.ExceptionEnum;
import com.szs.member.common.enums.SuccessEnum;
import com.szs.member.common.request.MemberLoginRequestDTO;
import com.szs.member.common.request.MemberSignUpRequestDTO;
import com.szs.member.common.response.*;
import com.szs.member.domain.Member;
import com.szs.member.jwt.JwtTokenProvider;
import com.szs.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    private static final Map<String, String> PERMIT_MAP =
            Collections.unmodifiableMap(new HashMap<String, String>() {{
                put("홍길동", "860824-1655068");
                put("김둘리", "921108-1582816");
                put("마징가", "880601-2455116");
                put("베지터", "910411-1656116");
                put("손오공", "820326-2715702");
                put("김시용", "123456-123456");
            }});



    @Transactional
    public ResponseDTO siginUp(MemberSignUpRequestDTO memberRequestDTO) {
        Member member = Member.createMember(memberRequestDTO, passwordEncoder);

        member.permitCheck(PERMIT_MAP, memberRequestDTO);
        validateDuplicated(member);

        memberRepository.save(member);
        return new ResponseDTO().createResponseDTO(SuccessEnum.SIGN_UP, new HashMap<>());
    }

    public void validateDuplicated(Member member) {
        if(Optional.ofNullable(memberRepository.findTop1ByUserIdOrRegNo(member.getUserId(),  member.getRegNo())).isPresent())
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
                        .doOnError((throwable -> {
                            throw new RuntimeException();
                         }))
                        .block();
    }
}

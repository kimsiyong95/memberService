package com.szs.member.controller;


import com.szs.member.common.enums.ExceptionEnum;
import com.szs.member.common.request.MemberLoginRequestDTO;
import com.szs.member.common.request.MemberSignUpRequestDTO;
import com.szs.member.common.response.ApiException;
import com.szs.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/szs/")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("signup")
    public ResponseEntity siginUp(@Valid @RequestBody MemberSignUpRequestDTO memberRequestDTO
            , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            throw new ApiException(ExceptionEnum.USER_PARAM_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.siginUp(memberRequestDTO));
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody MemberLoginRequestDTO memberLoginRequestDTO
            , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ApiException(ExceptionEnum.USER_PARAM_ERROR);
        }

        return ResponseEntity.ok().body(memberService.login(memberLoginRequestDTO));
    }

    @GetMapping("me")
    public ResponseEntity getMyInfo(Principal principal){
        return ResponseEntity.ok().body(memberService.getMyInfo(principal.getName()));
    }

    @PostMapping("scrap")
    public ResponseEntity getScrap(Principal principal){
        return ResponseEntity.ok().body(memberService.getScrap(principal.getName()));
    }


    @GetMapping("refund")
    public ResponseEntity getMyRefund(Principal principal){
        return ResponseEntity.ok().body(memberService.getMyRefund(principal.getName()));
    }


}

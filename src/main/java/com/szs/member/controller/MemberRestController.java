package com.szs.member.controller;


import com.szs.member.common.enums.ExceptionEnum;
import com.szs.member.common.request.MemberLoginRequestDTO;
import com.szs.member.common.request.MemberSignUpRequestDTO;
import com.szs.member.common.response.ApiException;
import com.szs.member.service.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Tag(name = "회원 API", description = "회원 API")
@RestController
@RequestMapping("/api/v1/szs/")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;


    @Operation(summary = "회원가입",
            responses = {
                    @ApiResponse(responseCode = "200", description = "200", content = @Content(schema = @Schema(implementation = Map.class),
                            examples = {@ExampleObject(value = "{\n" +
                                    " \"status\": 200,\n" +
                                    " \"message\": \"회원가입\" \n" +
                                    " \"data\": {}\n" +
                                    "}")})),
            })
    @PostMapping("signup")
    public ResponseEntity siginUp(@Valid @RequestBody MemberSignUpRequestDTO memberRequestDTO
            , BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            throw new ApiException(ExceptionEnum.USER_PARAM_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberService.siginUp(memberRequestDTO));
    }



    @Operation(summary = "로그인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "200", content = @Content(schema = @Schema(implementation = Map.class),
                            examples = {@ExampleObject(value = "{\n" +
                                    " \"status\": 200,\n" +
                                    " \"message\": \"로그인\" \n" +
                                    " \"data\": {\n" +
                                    " \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6ImRlZmF1bHQifQ.eyJzdWIi\n" +
                                    "   OiJ1c2VyIiwiaW5mbyI6eyJ1c2VyTm8iOjEsInVzZXJJZCI6ImhvbmcxMiJ9LCJleHAiOjE2NDc0OTkxODd9.n\n" +
                                    "   c9eKhGBn_jZZb0XbWsBMGIlfxwidHXuvIm70aIf2SA\" \n" +
                                    " \"type\": \"BEARER\"\n  }\n " +
                                    "}" )})),

            })
    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody MemberLoginRequestDTO memberLoginRequestDTO
            , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ApiException(ExceptionEnum.USER_PARAM_ERROR);
        }

        return ResponseEntity.ok().body(memberService.login(memberLoginRequestDTO));
    }


   
    @Operation(summary = "내정보조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "200", content = @Content(schema = @Schema(implementation = Map.class),
                            examples = {@ExampleObject(value = "{\n" +
                                    " \"status\": 200,\n" +
                                    " \"message\": \"내정보\" \n" +
                                    " \"data\": {\n" +
                                    " \"userNo\": 1 \n" +
                                    " \"userId\": \"kimsiyong\" \n" +
                                    " \"name\": \"홍길동\" \n" +
                                    " \"regNo\": \"860824-1655068\"\n  }\n" +
                                    "}" )})),

            })
    @GetMapping("me")
    public ResponseEntity getMyInfo(@ApiIgnore Principal principal){
        return ResponseEntity.ok().body(memberService.getMyInfo(principal.getName()));
    }


    @Operation(summary = "내스크랩조회및저장",
            responses = {
                    @ApiResponse(responseCode = "200", description = "200", content = @Content(schema = @Schema(implementation = Map.class),
                            examples = {@ExampleObject(value = "{\n" +
                                    " \"status\": 200,\n" +
                                    " \"message\": \"동기스크랩\" \n" +
                                    " \"data\": {\n" +
                                    "       \"jsonList\": { \n" +
                                    "           \"scrap001\": [{ \n" +
                                    "               \"소득내역\": \"급여\"\n"+
                                    "               \"총지급액\": \"2,4000.000\" \n"+
                                    "                           . \n"+
                                    "                           . \n"+
                                    "                           . \n"+
                                    "                           . \n"+
                                    "             }]\n" +
                                    "        } \n" +

                                    "}" )})),

            })
    @PostMapping("scrap")
    public ResponseEntity getScrap(@ApiIgnore Principal principal){
        return ResponseEntity.ok().body(memberService.getScrap(principal.getName()));
    }



    @Operation(summary = "내환급금조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "200", content = @Content(schema = @Schema(implementation = Map.class),
                            examples = {@ExampleObject(value = "{\n" +
                                    " \"status\": 200,\n" +
                                    " \"message\": \"환급액\" \n" +
                                    " \"data\": {\n" +
                                    " \"이름\": \"홍길동\" \n" +
                                    " \"한도\": \"68만 4천원\", \n" +
                                    " \"공제액\": \"92만 5천원\", \n" +
                                    " \"환급액\": \"68만 4천원\"\n  }\n" +
                                    "}" )})),

            })
    @GetMapping("refund")
    public ResponseEntity getMyRefund(@ApiIgnore Principal principal){
        return ResponseEntity.ok().body(memberService.getMyRefund(principal.getName()));
    }


}

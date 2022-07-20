package com.szs.member.common.error;


import com.szs.member.common.enums.ExceptionEnum;
import com.szs.member.common.response.ApiException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error/")
public class ErrorController {



    @GetMapping("auth-in")
    public void errorToken(HttpServletRequest request){
        throw new ApiException(ExceptionEnum.NON_AUTHENTICATION_ERROR);
    }
}

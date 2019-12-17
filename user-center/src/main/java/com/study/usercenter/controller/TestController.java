package com.study.usercenter.controller;

import com.study.usercenter.domain.dto.user.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/q")
    public UserDTO query(UserDTO userDTO){
        return userDTO;
    }
}

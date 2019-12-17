package com.study.usercenter.controller;

import com.study.usercenter.domain.dto.user.UserDTO;
import com.study.usercenter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor=@__({@Autowired}))
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable(name = "id") Integer id){
        log.info("findById被访问了");
        return userService.findById(id);
    }
}

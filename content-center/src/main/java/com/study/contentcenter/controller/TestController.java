package com.study.contentcenter.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.study.contentcenter.domain.dto.user.UserDTO;
import com.study.contentcenter.feign.TestFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestFeignClient client;

    @GetMapping("/q")
    public UserDTO query(UserDTO userDTO){
        return client.query(userDTO);
    }

    @SentinelResource(value = "sentinel-resource",blockHandler = "block",fallback = "fallback")
    @GetMapping("/sentinel-resource")
    public String testSentinelResource(@RequestParam(required = false) String a){
        if(StringUtils.isBlank(a)){
            throw new IllegalArgumentException("a can not be blank.");
        }
        return "success";
    }

    public String block(String a, BlockException e){
        log.info("block...",e);
        return "block";
    }

    public String fallback(String a, Throwable e){
        log.info("fallback...",e);
        return "fallback";
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/sentinel-rest-template")
    public UserDTO sentinelRestTemplate(@RequestParam(name = "id") Integer id){
        return restTemplate.getForObject("http://user-center/users/{id}", UserDTO.class, id);
    }
}

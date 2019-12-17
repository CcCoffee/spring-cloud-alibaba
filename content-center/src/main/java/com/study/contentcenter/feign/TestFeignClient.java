package com.study.contentcenter.feign;

import com.study.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "user-center")
public interface TestFeignClient {

    @GetMapping("/test/q")
    UserDTO query(@SpringQueryMap UserDTO userDTO);
}

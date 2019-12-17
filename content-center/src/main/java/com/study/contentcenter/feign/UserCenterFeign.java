package com.study.contentcenter.feign;


import com.study.contentcenter.domain.dto.user.UserDTO;
import com.study.contentcenter.feign.fallback.UserCenterFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "user-center", configuration = UserCenterFeignConfiguration.class)
//@FeignClient(name = "user-center", fallback = UserCenterFeignFallback.class)
@FeignClient(name = "user-center", fallbackFactory = UserCenterFeignFallbackFactory.class)
public interface UserCenterFeign {

    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable("id") Integer id);
}

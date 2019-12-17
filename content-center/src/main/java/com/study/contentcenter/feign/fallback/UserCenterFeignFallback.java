package com.study.contentcenter.feign.fallback;

import com.study.contentcenter.domain.dto.user.UserDTO;
import com.study.contentcenter.feign.UserCenterFeign;
import org.springframework.stereotype.Component;

@Component
public class UserCenterFeignFallback implements UserCenterFeign {

    @Override
    public UserDTO findById(Integer id) {
        return UserDTO.builder().wxNickname("默认的昵称").build();
    }
}

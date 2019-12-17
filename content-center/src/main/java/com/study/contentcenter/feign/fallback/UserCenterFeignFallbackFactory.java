package com.study.contentcenter.feign.fallback;

import com.study.contentcenter.domain.dto.user.UserDTO;
import com.study.contentcenter.feign.UserCenterFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCenterFeignFallbackFactory implements FallbackFactory<UserCenterFeign> {
    @Override
    public UserCenterFeign create(Throwable throwable) {
        log.warn("fallback...",throwable);
        return new UserCenterFeign() {
            @Override
            public UserDTO findById(Integer id) {
                return UserDTO.builder().wxNickname("默认的昵称").build();
            }
        };
    }
}

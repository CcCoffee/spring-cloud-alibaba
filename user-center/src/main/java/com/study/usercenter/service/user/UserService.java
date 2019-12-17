package com.study.usercenter.service.user;

import com.study.usercenter.dao.user.UserMapper;
import com.study.usercenter.domain.dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;

    public UserDTO findById(Integer id) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userMapper.selectByPrimaryKey(id),userDTO);
        return userDTO;
    }
}

package com.study.usercenter.listener;

import com.study.usercenter.dao.user.BonusEventLogMapper;
import com.study.usercenter.dao.user.UserMapper;
import com.study.usercenter.domain.dto.message.UserAddBonusMsgDTO;
import com.study.usercenter.domain.entity.user.BonusEventLog;
import com.study.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RocketMQMessageListener(consumerGroup = "test-consumer",topic = "add_bonus")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserBonusListener implements RocketMQListener<UserAddBonusMsgDTO> {

    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;

    @Override
    public void onMessage(UserAddBonusMsgDTO message) {
        User user = userMapper.selectByPrimaryKey(message.getUserId());
        user.setBonus(user.getBonus() + message.getBonus());
        userMapper.updateByPrimaryKeySelective(user);
        bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .event("contribute")
                        .description("发布文章添加积分")
                        .userId(user.getId())
                        .value(message.getBonus())
                        .createTime(new Date())
                        .build());
    }
}

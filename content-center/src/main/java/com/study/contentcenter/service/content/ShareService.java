package com.study.contentcenter.service.content;

import com.study.contentcenter.dao.content.ShareMapper;
import com.study.contentcenter.domain.dto.content.ShareAuditDTO;
import com.study.contentcenter.domain.dto.content.ShareDTO;
import com.study.contentcenter.domain.dto.message.UserAddBonusMsgDTO;
import com.study.contentcenter.domain.dto.user.UserDTO;
import com.study.contentcenter.domain.entity.content.Share;
import com.study.contentcenter.feign.UserCenterFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static com.study.contentcenter.domain.enums.ShareAuditStatus.NOT_YET;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {

    private final UserCenterFeign userCenterFeign;
    private final ShareMapper shareMapper;
    private final RocketMQTemplate rocketMQTemplate;
//    private final RestTemplate restTemplate;
//    private final DiscoveryClient discoveryClient;

    public ShareDTO findById(Integer id){
        Share share = shareMapper.selectByPrimaryKey(id);

        //1. 使用RestTemplate方式
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        List<String> urls = instances.stream().map(item -> item.getUri() + "/users/{id}").collect(Collectors.toList());
//        String targetUrl = urls.get(ThreadLocalRandom.current().nextInt(urls.size()));
//        log.info(targetUrl);
//        UserDTO userDTO = restTemplate.getForObject(targetUrl, UserDTO.class, share.getUserId());

        //2. RestTemplate使用Ribbion实现负载均衡
        //UserDTO userDTO = restTemplate.getForObject("http://user-center/users/{id}", UserDTO.class, share.getUserId());

        //3. UserCenterFeign
        UserDTO userDTO = userCenterFeign.findById(id);
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share,shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

    public Share auditById(Integer shareId, ShareAuditDTO shareAuditDTO) {
        Share share = shareMapper.selectByPrimaryKey(shareId);
        if(share == null){
            throw new IllegalArgumentException("非法参数，该分享不存在");
        }
        if(shareAuditDTO.getShareAuditStatus().toString().equals(NOT_YET.toString())){
            throw new IllegalArgumentException("非法参数，必须为为审核状态");
        }
        share.setAuditStatus(shareAuditDTO.getShareAuditStatus().toString());
        share.setReason(shareAuditDTO.getReason());
        share.setUpdateTime(new Date());
        shareMapper.updateByPrimaryKeySelective(share);
        rocketMQTemplate.asyncSend("add_bonus",
                UserAddBonusMsgDTO.builder()
                        .userId(share.getUserId())
                        .bonus(50)
                        .build(), new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("send message success");
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        log.warn("send message failed",throwable);
                    }
                });
        return share;
    }
}

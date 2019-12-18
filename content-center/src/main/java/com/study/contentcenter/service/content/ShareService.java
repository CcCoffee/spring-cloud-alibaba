package com.study.contentcenter.service.content;

import com.study.contentcenter.dao.content.RocketmqTransactionLogMapper;
import com.study.contentcenter.dao.content.ShareMapper;
import com.study.contentcenter.domain.dto.content.ShareAuditDTO;
import com.study.contentcenter.domain.dto.content.ShareDTO;
import com.study.contentcenter.domain.dto.message.UserAddBonusMsgDTO;
import com.study.contentcenter.domain.dto.user.UserDTO;
import com.study.contentcenter.domain.entity.content.RocketmqTransactionLog;
import com.study.contentcenter.domain.entity.content.Share;
import com.study.contentcenter.feign.UserCenterFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

import static com.study.contentcenter.domain.enums.ShareAuditStatus.NOT_YET;
import static com.study.contentcenter.domain.enums.ShareAuditStatus.PASS;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {

    private final UserCenterFeign userCenterFeign;
    private final ShareMapper shareMapper;
    private final RocketMQTemplate rocketMQTemplate;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;
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
        if(!share.getAuditStatus().equals(NOT_YET.toString())){
            throw new IllegalArgumentException("非法参数，必须为为审核状态");
        }

        if(PASS.toString().equals(shareAuditDTO.getShareAuditStatus().toString())){
            String transactionId = UUID.randomUUID().toString();
            rocketMQTemplate.sendMessageInTransaction(
                    "tx-add-bonus-group",
                    "add_bonus",
                    MessageBuilder.withPayload(
                            UserAddBonusMsgDTO.builder()
                                    .userId(share.getUserId())
                                    .bonus(50)
                                    .build())
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                            .setHeader("share_id",share.getId()).build(),
                    shareAuditDTO);
        }else{
            this.auditByIdInDB(shareId,shareAuditDTO);
        }
        return share;
    }

    public void auditByIdInDB(Integer shareId, ShareAuditDTO shareAuditDTO){
        Share share = Share.builder()
                .id(shareId)
                .reason(shareAuditDTO.getReason())
                .auditStatus(shareAuditDTO.getShareAuditStatus().toString())
                .build();
        shareMapper.updateByPrimaryKeySelective(share);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditByIdInDBWithTransactionLog(Integer shareId, ShareAuditDTO shareAuditDTO,String transactionId){
        this.auditByIdInDB(shareId,shareAuditDTO);
        rocketmqTransactionLogMapper.insert(
                RocketmqTransactionLog
                        .builder()
                        .transactionId(transactionId)
                        .log("记录日志")
                        .build());
    }
}

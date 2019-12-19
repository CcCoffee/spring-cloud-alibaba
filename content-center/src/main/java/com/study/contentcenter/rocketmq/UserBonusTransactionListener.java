package com.study.contentcenter.rocketmq;

import com.study.contentcenter.dao.content.RocketmqTransactionLogMapper;
import com.study.contentcenter.domain.dto.content.ShareAuditDTO;
import com.study.contentcenter.domain.entity.content.RocketmqTransactionLog;
import com.study.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@RocketMQTransactionListener(txProducerGroup="tx-add-bonus-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserBonusTransactionListener implements RocketMQLocalTransactionListener {

    private final ShareService shareService;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try{
            Integer shareId = Integer.valueOf((String)msg.getHeaders().get("share_id"));
            String transactionId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
            shareService.auditByIdInDBWithTransactionLog(shareId, (ShareAuditDTO) arg,transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 查询本地事务是否已经commit
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String transactionId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        RocketmqTransactionLog rocketmqTransactionLog = rocketmqTransactionLogMapper.selectOne(
                RocketmqTransactionLog
                        .builder()
                        .transactionId(transactionId)
                        .build()
        );
        if(rocketmqTransactionLog != null){
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}

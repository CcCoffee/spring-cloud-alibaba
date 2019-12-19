package com.study.usercenter.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StreamMessageListener {

    @StreamListener(Sink.INPUT)
    public void receive(String messageBody){
        log.info("message body : {}", messageBody);
    }
}

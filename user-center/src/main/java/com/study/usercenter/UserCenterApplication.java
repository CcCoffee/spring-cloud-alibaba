package com.study.usercenter;

import com.study.usercenter.rocketmq.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.study.usercenter")
@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding({Sink.class, MySink.class})
public class UserCenterApplication
{
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}

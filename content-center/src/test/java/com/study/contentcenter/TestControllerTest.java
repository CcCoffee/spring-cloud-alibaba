package com.study.contentcenter;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class TestControllerTest {

    @Test
    public void testSentinelGuanlian() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10000; i++) {
            restTemplate.getForObject("http://localhost:8010/test/q?id=1",String.class);
            Thread.sleep(2000);
        }
    }
}

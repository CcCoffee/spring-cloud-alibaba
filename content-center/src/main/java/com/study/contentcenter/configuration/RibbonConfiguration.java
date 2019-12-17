package com.study.contentcenter.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonGlobalConfiguration;


@Configuration
public class RibbonConfiguration {

    @RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
    @ConditionalOnProperty(name = "ribbon.user-center.enable",havingValue = "true")
    class UserCenterConfiguration{

    }

    @RibbonClients(defaultConfiguration = RibbonGlobalConfiguration.class)
//    @ConditionalOnProperty(name = "ribbon.global.enable",havingValue = "true")
    class GlobalConfiguration{
    }
}

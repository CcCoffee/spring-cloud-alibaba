package com.study.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class NacosSameZoneRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        try {
            String clusterName = nacosDiscoveryProperties.getClusterName();
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            String serviceName = loadBalancer.getName();
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            List<Instance> instances = namingService.selectInstances(serviceName, true);
            List<Instance> targetInstances;
            List<Instance> sameClusterServers = instances.stream().filter(item -> item.getClusterName().equals(clusterName)).collect(Collectors.toList());
            if(sameClusterServers.isEmpty()){
                targetInstances = instances;
                log.warn("【访问跨机房实例】");
            }else{
                targetInstances = sameClusterServers;
            }
            Instance instance = NacosSameZoneWeightBalancer.getHostByRandomWeight2(targetInstances);
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("Nacos权重访问异常",e);
        }
        return null;
    }
}

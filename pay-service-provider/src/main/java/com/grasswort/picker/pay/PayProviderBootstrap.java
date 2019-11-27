package com.grasswort.picker.pay;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuliangliang
 * @Classname PayProviderBootstrap
 * @Description 服务提供者启动类
 * @Date 2019/11/27 13:38
 * @blame Java Team
 */
@SpringBootApplication
@NacosPropertySource(dataId = "test.properties", autoRefreshed = true, groupId = "DEFAULT_GROUP")
public class PayProviderBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(PayProviderBootstrap.class, args);
    }
}

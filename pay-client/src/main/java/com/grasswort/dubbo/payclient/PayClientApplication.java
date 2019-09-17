package com.grasswort.dubbo.payclient;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "example", groupId = "${nacos.config.group}", autoRefreshed = true)
//@NacosPropertySource(dataId = "application.properties", groupId = "${nacos.config.group}")
public class PayClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayClientApplication.class, args);
    }

}

package com.grasswort.dubbo.payclient;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.grasswort.dubbo.IPayService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * PayController
 *
 * @blame Java Team
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private IPayService ipayService;
    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @GetMapping
    public String getPayResult(
            @RequestParam("info") String info
    ) {
        return ipayService.pay(info);
    }

    @GetMapping("/configuration")
    public String getPayConfiguration() {
        return String.valueOf(useLocalCache);
    }
}

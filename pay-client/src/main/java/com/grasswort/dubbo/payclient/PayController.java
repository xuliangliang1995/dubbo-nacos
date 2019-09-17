package com.grasswort.dubbo.payclient;

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

    @GetMapping
    public String getPayResult(
            @RequestParam("info") String info
    ) {
        return ipayService.pay(info);
    }
}

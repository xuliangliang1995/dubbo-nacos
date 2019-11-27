package com.grasswort.picker.pay.controller;

import com.grasswort.picker.pay.PayService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuliangliang
 * @Classname PayController
 * @Description 支付 API
 * @Date 2019/11/27 14:13
 * @blame Java Team
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference(version = "1.0")
    PayService payService;

    @GetMapping("/{order}")
    public String pay(@PathVariable("order") String order) {
        return payService.payOrder(order);
    }
}

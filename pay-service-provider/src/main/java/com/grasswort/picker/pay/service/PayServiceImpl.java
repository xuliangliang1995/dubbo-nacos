package com.grasswort.picker.pay.service;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.grasswort.picker.pay.PayService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author xuliangliang
 * @Classname PayServiceImpl
 * @Description 支付服务
 * @Date 2019/11/27 13:43
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 5000)
public class PayServiceImpl implements PayService {
    @NacosValue(value = "${test.pay.method:wechat}", autoRefreshed = true)
    private String payMethod;
    /**
     * 订单支付
     *
     * @param order 订单信息
     * @return 订单支付结果
     */
    @Override
    public String payOrder(String order) {
        return order.concat("支付成功").concat("【" + payMethod + "】");
    }
}

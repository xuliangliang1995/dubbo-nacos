package com.grasswort.picker.pay;

/**
 * @author xuliangliang
 * @Classname PayService
 * @Description 支付服务
 * @Date 2019/11/27 12:54
 * @blame Java Team
 */
public interface PayService {
    /**
     * 订单支付
     * @param order 订单信息
     * @return  订单支付结果
     */
    String payOrder(String order);
}

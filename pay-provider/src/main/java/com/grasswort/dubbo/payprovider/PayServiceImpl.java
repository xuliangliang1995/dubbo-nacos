package com.grasswort.dubbo.payprovider;

import com.grasswort.dubbo.IPayService;
import org.apache.dubbo.config.annotation.Service;

/**
 * PayServiceImpl
 *
 * @blame Java Team
 */
@Service
public class PayServiceImpl implements IPayService {

    @Override
    public String pay(String info) {
        System.out.println("execute pay ：" + info);
        return "PAY_SUCCESS ：" + info;
    }
}

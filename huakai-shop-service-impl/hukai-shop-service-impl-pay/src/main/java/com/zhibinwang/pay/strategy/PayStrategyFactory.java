package com.zhibinwang.pay.strategy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhibin.wang
 * @create 2019-11-25
 * @desc 支付策略模式工厂类
 **/
@Slf4j
public class PayStrategyFactory {

    private static final Map<String,PayStrategy> map = new ConcurrentHashMap<>();

    public static  PayStrategy getPayStrategy(String className) {
        if (StringUtils.isBlank(className)) {
            return null;
        }
        PayStrategy payStrategy = map.get(className);
        if (payStrategy != null) {
            return payStrategy;
        }
        Class<PayStrategy> aClass = null;
        try {
            aClass = (Class<PayStrategy>) Class.forName(className);
            payStrategy = aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取支付策略异常：", e);
            return null;


        }
        map.put(className,payStrategy);
        return  payStrategy;
    }
}

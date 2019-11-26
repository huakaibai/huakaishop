package com.zhibinwang.pay.config;

import com.alipay.sdk.AliPayConfig;
import com.union.sdk.SDKConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhibin.wang
 * @create 2019-11-26 15:40
 * @desc 服务启动后执行的代码
 **/
@Component
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("加载阿里支付，银联支付配置文件");

        SDKConfig.getConfig().loadPropertiesFromSrc();
        AliPayConfig.getAliPayConfig().loadProperties();

    }
}

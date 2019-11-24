package com.zhibinwang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author 花开
 * @create 2019-11-22 21:38
 * @desc
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.zhibinwang.pay.mapper")
public class AppPay {

    public static void main(String[] args) {
       SpringApplication.run(AppPay.class, args);
    }
}

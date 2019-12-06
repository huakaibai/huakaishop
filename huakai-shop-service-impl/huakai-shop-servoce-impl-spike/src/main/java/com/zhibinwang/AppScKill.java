package com.zhibinwang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author 花开
 * @create 2019-11-22 21:38
 * @desc
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.zhibinwang.spike.mapper")
public class AppScKill {

    public static void main(String[] args) {
       SpringApplication.run(AppScKill.class, args);
    }
}

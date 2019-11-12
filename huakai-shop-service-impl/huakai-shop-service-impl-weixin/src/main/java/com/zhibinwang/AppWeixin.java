package com.zhibinwang;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 花开
 * @create 2019-10-23 21:20
 * @desc
 **/
@SpringCloudApplication

@EnableApolloConfig
public class AppWeixin {

    public static void main(String[] args) {
        SpringApplication.run(AppWeixin.class,args);
    }
}

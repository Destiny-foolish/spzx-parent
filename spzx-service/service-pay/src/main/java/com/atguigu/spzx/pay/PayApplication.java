package com.atguigu.spzx.pay;


import com.atguigu.spzx.common.anno.EnableUserTokenFeignInterceptor;
import com.atguigu.spzx.common.anno.EnableUserWebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableUserWebMvcConfiguration
@EnableFeignClients(basePackages = {
        "com.atguigu.spzx.feign.order",
        "com.atguigu.spzx.feign.product"
})
@EnableUserTokenFeignInterceptor
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class , args) ;
    }

}
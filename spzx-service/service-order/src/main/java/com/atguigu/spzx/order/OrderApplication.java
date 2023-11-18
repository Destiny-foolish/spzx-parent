package com.atguigu.spzx.order;

import com.atguigu.spzx.common.anno.EnableUserTokenFeignInterceptor;
import com.atguigu.spzx.common.anno.EnableUserWebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableUserTokenFeignInterceptor
@EnableUserWebMvcConfiguration
@EnableFeignClients(basePackages = {
        "com.atguigu.spzx.feign.cart",
        "com.atguigu.spzx.feign.user",
        "com.atguigu.spzx.feign.product"
})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class , args) ;
    }
}

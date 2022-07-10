package com.icecocoeci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created with IntelliJ IDEA.
 *
 * @auther wlf
 * @Date: 2022/06/05/11:46
 * @Description:
 */
@SpringBootApplication
//作用是扫描mybatis通用mapper所在的包
@MapperScan(basePackages = "com.icecocoeci.mapper")
public class FoodieApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodieApplication.class,args);
    }
}

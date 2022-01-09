package com.zxm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zxm.mapper")
public class CasBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasBlogApplication.class, args);
    }

}

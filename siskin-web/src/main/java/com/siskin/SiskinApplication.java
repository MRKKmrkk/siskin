package com.siskin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.siskin.mapper")
public class SiskinApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiskinApplication.class, args);
    }
}

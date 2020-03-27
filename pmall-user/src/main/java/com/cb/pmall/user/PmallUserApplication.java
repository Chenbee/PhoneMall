package com.cb.pmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.cb.pmall.user.mapper")
public class PmallUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmallUserApplication.class, args);
    }

}

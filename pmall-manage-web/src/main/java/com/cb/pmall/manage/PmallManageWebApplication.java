package com.cb.pmall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
public class PmallManageWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmallManageWebApplication.class, args);
    }

}

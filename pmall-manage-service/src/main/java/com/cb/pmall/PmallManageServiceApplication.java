package com.cb.pmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.cb.pmall.manage.mapper")
public class PmallManageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmallManageServiceApplication.class, args);
    }

}

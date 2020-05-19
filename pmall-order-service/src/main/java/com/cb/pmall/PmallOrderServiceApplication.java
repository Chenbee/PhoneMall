package com.cb.pmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.cb.pmall.order.mapper")
public class PmallOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmallOrderServiceApplication.class, args);
    }

}

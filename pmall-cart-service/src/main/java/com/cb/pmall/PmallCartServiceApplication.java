package com.cb.pmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.cb.pmall.cart.mapper")
public class PmallCartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmallCartServiceApplication.class, args);
    }

}

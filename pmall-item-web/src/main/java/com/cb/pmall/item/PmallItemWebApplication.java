package com.cb.pmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cb.pmall")
public class PmallItemWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmallItemWebApplication.class, args);
    }

}

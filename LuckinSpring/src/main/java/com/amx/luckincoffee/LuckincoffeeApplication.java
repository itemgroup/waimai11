package com.amx.luckincoffee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.amx.luckincoffee.dao")
public class LuckincoffeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckincoffeeApplication.class, args);
    }

}

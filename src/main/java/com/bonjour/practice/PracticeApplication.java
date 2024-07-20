package com.bonjour.practice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@MapperScan("com.bonjour.practice.**.mapper")
@SpringBootApplication(scanBasePackages = "com.bonjour.practice")
public class PracticeApplication {

    //
    public static void main(String[] args) {
        SpringApplication.run(PracticeApplication.class, args);
    }

}

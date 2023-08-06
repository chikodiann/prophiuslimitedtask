package com.ann.prophiuslimitedtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ann.prophiuslimitedtask.mapper")
public class ProphiusLimitedTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProphiusLimitedTaskApplication.class, args);
    }

}

package com.awsdemo.demo;

import com.awsdemo.demo.ecxutePool.TaskThreadPoolConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.function.Function;

@SpringBootApplication
@EnableConfigurationProperties(TaskThreadPoolConfig.class)
public class DemoApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

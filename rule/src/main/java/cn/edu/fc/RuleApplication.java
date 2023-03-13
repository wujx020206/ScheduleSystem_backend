package cn.edu.fc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cn.edu.fc.javaee.core",
        "cn.edu.fc.schedulesystem.rule"})
@MapperScan("cn.edu.xmu.schedulesystem.rule.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class RuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuleApplication.class, args);
    }
}
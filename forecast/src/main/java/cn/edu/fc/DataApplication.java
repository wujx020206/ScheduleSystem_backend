package cn.edu.fc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cn.edu.fc.javaee.core",
        "cn.edu.fc.schedulesystem.data"})
@MapperScan("cn.edu.xmu.schedulesystem.data.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }
}
package cn.edu.fc;

import cn.edu.fc.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"cn.edu.fc.javaee.core",
        "cn.edu.fc.store"}, exclude = {DataSourceAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@ComponentScan(basePackages = {"cn.edu.fc.dao"})
@MapperScan(basePackages = "cn.edu.fc.dao")
@EnableJpaRepositories(value = "cn.edu.fc.javaee.core.jpa", repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.fc.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class StoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }
}
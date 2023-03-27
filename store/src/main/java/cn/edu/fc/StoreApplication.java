package cn.edu.fc;

import cn.edu.fc.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.persistence.Entity;

@SpringBootApplication(scanBasePackages = {"cn.edu.fc.javaee.core",
        "cn.edu.fc.schedulesystem.store"})
@ComponentScan(basePackages = {"cn.edu.fc.*"})
//@MapperScan(basePackages = {"cn.edu.fc.mapper"})
@EnableJpaRepositories(value = {"cn.edu.fc.dao"}, repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.fc.mapper")
@EntityScan("cn.edu.fc.*")
@EnableMongoRepositories(basePackages = "cn.edu.fc.schedulesystem.store.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class StoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }
}
package dev.emoforge.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "dev.emoforge")
@EnableJpaRepositories(basePackages = "dev.emoforge")
@EntityScan(basePackages = "dev.emoforge")
@MapperScan("dev.emoforge.post.mapper")
@ConfigurationPropertiesScan
public class EmoforgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmoforgeApplication.class, args);
    }
}

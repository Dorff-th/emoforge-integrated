package dev.emoforge.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        // 1. 모든 모듈(core, auth, post 등)의 Bean을 스캔하기 위해 최상위 패키지 지정
        scanBasePackages = "dev.emoforge"
)
// 1. Repository 빈 스캔 경로 설정
@EnableJpaRepositories(basePackages = "dev.emoforge")
// 2. @Entity 클래스 스캔 경로 설정
@EntityScan(basePackages = "dev.emoforge")
@EnableFeignClients(basePackages = "dev.emoforge.auth.service.external")
public class EmoforgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmoforgeApplication.class, args);
    }
}

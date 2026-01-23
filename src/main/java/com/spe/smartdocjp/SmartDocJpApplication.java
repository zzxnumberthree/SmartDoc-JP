package com.spe.smartdocjp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing // 可以自动帮我们填充 created_at 和 updated_at
@EnableAspectJAutoProxy
public class SmartDocJpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartDocJpApplication.class, args);
    }

}

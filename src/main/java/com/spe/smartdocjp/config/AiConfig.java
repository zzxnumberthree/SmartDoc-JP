package com.spe.smartdocjp.config;

import com.google.genai.Client;
import com.google.genai.types.HttpOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AiConfig {

    // 在类初始化时就设置好系统代理
    static {
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7890");
    }

    @Bean
    public Client googleGenAiClient(){

        String googleApiKey = System.getenv("GOOGLE_API_KEY");

        // 1. 配置 HTTP 选项，设置超时时间
        HttpOptions httpOptions = HttpOptions.builder()
                .timeout((int) Duration.ofSeconds(60).toMillis()) // 设置超时为 60 秒 (单位是毫秒)
                .build();

        // 2. 创建并返回 Client
        return Client.builder()
                .apiKey(googleApiKey)
                .httpOptions(httpOptions)
                .build();
    }
}
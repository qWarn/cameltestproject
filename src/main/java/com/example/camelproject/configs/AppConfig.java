package com.example.camelproject.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jms.annotation.EnableJms;

import java.util.Random;

@Configuration
@EnableJms
@EnableAspectJAutoProxy
public class AppConfig {


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Random random() {
        return new Random();
    }

}

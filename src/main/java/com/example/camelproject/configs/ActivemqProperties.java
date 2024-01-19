package com.example.camelproject.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.activemq.queues")
@Setter
@Getter
public class ActivemqProperties {

    private String camelQueueName;

}

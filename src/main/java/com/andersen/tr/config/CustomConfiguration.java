package com.andersen.tr.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfiguration {

    @Bean
    @ConditionalOnProperty(name = "custom.conditional.property",
            havingValue = "true")
    public String conditionalBean() {
        return new String("Bean is true!");
    }
}

package com.unicred.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor
@Configuration
public class AppConfiguration {

    @Value("${topics.create-ticket}")
    private String topicCreateTicket;
}

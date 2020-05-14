package com.holiday.touristadviceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TouristAdviceServiceApplication {

    public static void main(String[] args) throws TelegramApiRequestException, InterruptedException, AccountNotFoundException {
        ApiContextInitializer.init();
        SpringApplication.run(TouristAdviceServiceApplication.class, args);
    }
}

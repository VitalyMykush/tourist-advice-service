package com.holiday.touristadviceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class TouristAdviceServiceApplication {

	public static void main(String[] args) throws TelegramApiRequestException {
		ApiContextInitializer.init();
		SpringApplication.run(TouristAdviceServiceApplication.class, args);
	}
	}

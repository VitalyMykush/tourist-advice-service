package com.holiday.touristadviceservice;

import com.holiday.touristadviceservice.bot.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


@SpringBootApplication
@Import(BotConfig.class)
public class TouristAdviceServiceApplication {

	public static void main(String[] args) throws TelegramApiRequestException {
		SpringApplication.run(TouristAdviceServiceApplication.class, args);
	}
	}

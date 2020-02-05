package com.holiday.touristadviceservice.bot;

import com.holiday.touristadviceservice.bot.TouristAdviceBot;
import com.holiday.touristadviceservice.repository.CityRepository;
import com.holiday.touristadviceservice.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Configuration
public class BotConfig {

    @Autowired
    CityService cityService;

    @PostConstruct
    public void init( ) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        TouristAdviceBot touristAdviceBot = new TouristAdviceBot();
        touristAdviceBot.setCityRepository(cityService);
        botsApi.registerBot(touristAdviceBot);
    }

}

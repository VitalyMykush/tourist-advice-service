package com.holiday.touristadviceservice.bot;

import com.holiday.touristadviceservice.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TouristAdviceBotConfig {

    @Value("${telegram.token}")
    private String token;

    private final CityService cityService;

    @PostConstruct
    public void init() {
        ApiContextInitializer.init();
    }

    @Bean
    public TelegramLongPollingBot touristAdviceBot(){
        return new TouristAdviceBot(cityService,token);
    }

}

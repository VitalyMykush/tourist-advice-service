package com.holiday.touristadviceservice.bot;

import com.holiday.touristadviceservice.entity.City;
import com.holiday.touristadviceservice.entity.CityNotFoundException;
import com.holiday.touristadviceservice.service.CityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.util.List;
@Component
public class TouristAdviceBot extends TelegramLongPollingBot {

    public static final String TOKEN = "1001031741:AAFE_0no7KD8muGxAxdqh_MYySpW1OQf644";
    private static final String ADVICE_NOT_FOUND = "К сожалению я не могу вам ничего посоветовать(";

    private CityService cityService;

    @Autowired
    public TouristAdviceBot(CityService cityService){
        this.cityService = cityService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                String cityName = inMessage.getText();
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId());
                try{
                    execute(outMessage.setText(getResponse(cityService.read(cityName))));
                }catch (CityNotFoundException cityNotFoundException){
                    execute(outMessage.setText(cityNotFoundException.getMessage()));
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getResponse(List<City> cities) {
        if (cities.size() == 1) {
            return cities.get(0).getAdvices().stream().reduce((s1,s2) -> s1.concat(s2)).orElse(ADVICE_NOT_FOUND);
        } else {
            StringBuilder response = new StringBuilder("В мире много городов с таким названием\n");
            for (City c : cities) {
                response.append(c.getName()).append(" (").append(c.getId()).append(")\n");
                response.append(c.getAdvices().stream().reduce((s1, s2) -> s1.concat("\n").concat(s2)).orElse(ADVICE_NOT_FOUND));
                response.append("\n\n");
            }
            return response.toString();
        }
    }

    @Override
    public String getBotUsername() {
        return "TouristAdviceBot";
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}

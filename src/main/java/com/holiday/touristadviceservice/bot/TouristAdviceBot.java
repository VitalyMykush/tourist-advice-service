package com.holiday.touristadviceservice.bot;

import com.holiday.touristadviceservice.entity.Advice;
import com.holiday.touristadviceservice.entity.City;
import com.holiday.touristadviceservice.entity.CityNotFoundException;
import com.holiday.touristadviceservice.service.CityService;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class TouristAdviceBot extends TelegramLongPollingBot {
    public static final String TOKEN = "1001031741:AAFE_0no7KD8muGxAxdqh_MYySpW1OQf644";
    private static final String ADVICE_NOT_FOUND = "К сожалению я не могу вам ничего посоветовать(";
    private CityService cityService;

    public void setCityRepository(CityService cityService) {
        this.cityService = cityService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                String cityName = inMessage.getText();
                List<City> cities = cityService.read(cityName);
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId());
                execute(outMessage.setText(getResponse(cities)));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }catch (CityNotFoundException c){
            Message inMessage = update.getMessage();
            SendMessage outMessage = new SendMessage();
            outMessage.setChatId(inMessage.getChatId());
            execute(outMessage.setText(c.getMessage()));
        }
    }

    private String getResponse(List<City> cities) {
        if (cities.size() == 0) {
            return ADVICE_NOT_FOUND;
        } else if (cities.size() == 1) {
            StringBuilder response = new StringBuilder();
            for (Advice advice : cities.get(0).getAdvices()) {
                response.append(advice.getAdvice()).append("\n");
            }
            if (response.toString().isEmpty())
                return ADVICE_NOT_FOUND;
            else
                return response.toString();
        } else {
            StringBuilder response = new StringBuilder("В мире много городов с таким названием\n");
            for (City c : cities) {
                response.append(c.getName()).append(" (").append(c.getId()).append(")\n");
                if (!c.getAdvices().isEmpty()) {
                    for (Advice advice : c.getAdvices()) {
                        response.append(advice.getAdvice()).append("\n");
                    }
                }
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

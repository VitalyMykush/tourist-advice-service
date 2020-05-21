package com.holiday.touristadviceservice.bot;

import com.holiday.touristadviceservice.entity.City;
import com.holiday.touristadviceservice.entity.CityNotFoundException;
import com.holiday.touristadviceservice.service.CityService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class TouristAdviceBot extends TelegramLongPollingBot {

    private static final String ADVICE_NOT_FOUND_MESSAGE = "К сожалению я не могу вам ничего посоветовать. Для данного города нет советов.";
    private static final String MANY_CITY_WITH_NAME_MESSAGE = "В мире много городов с таким названием";
    private static final String CITY_NOT_FOUND_MESSAGE = "К сожалению город не найден";

    private String TOKEN;
    private String botUsername = "TouristAdviceBot";
    private CityService cityService;

    public TouristAdviceBot(CityService cityService, String token) {
        if (cityService == null) {
            throw new IllegalArgumentException("CityService cannot be null");
        }
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Telegram token cannot be null or empty");
        }
        this.cityService = cityService;
        this.TOKEN = token;
    }

    public TouristAdviceBot(CityService cityService, String token, String botUsername) {
        this(cityService, token);
        if (botUsername == null || botUsername.isEmpty()) {
            throw new IllegalArgumentException("botUsername cannot be null or empty");
        }
        this.botUsername = botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                String cityName = inMessage.getText();
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId());
                try {
                    execute(outMessage.setText(getResponse(cityService.read(cityName))));
                } catch (CityNotFoundException cityNotFoundException) {
                    execute(outMessage.setText(CITY_NOT_FOUND_MESSAGE));
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getResponse(List<City> cities) {
        if (cities.size() == 1) {
            return cities.get(0).getAdvices().stream().reduce(String::concat).orElse(ADVICE_NOT_FOUND_MESSAGE);
        } else {
            StringBuilder response = new StringBuilder(MANY_CITY_WITH_NAME_MESSAGE).append("\n");
            for (City c : cities) {
                response.append(c.getName()).append(" (").append(c.getId()).append(")\n");
                response.append(c.getAdvices().stream().reduce((s1, s2) -> s1.concat("\n").concat(s2)).orElse(ADVICE_NOT_FOUND_MESSAGE));
                response.append("\n\n");
            }
            return response.toString();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}

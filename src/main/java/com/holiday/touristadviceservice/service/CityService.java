package com.holiday.touristadviceservice.service;

import com.holiday.touristadviceservice.entity.City;
import com.holiday.touristadviceservice.entity.CityNotFoundException;

import java.util.List;

public interface CityService {
    /**
     * Создает нового города
     * @param city - city для создания
     */
    City create(City city);

    /**
     * Возвращает список всех имеющихся городов
     * @return список городов
     * @throws - CityNotFoundException если не один город не был найден
     */
    List<City> readAll() throws CityNotFoundException;

    /**
     * Возвращает город по его ID
     * @param id - ID города
     * @return - объект города с заданным ID
     * @throws - CityNotFoundException если объект с таким ID не найден
     */
    City read(long id) throws CityNotFoundException;

    /**
     * Возвращает города с заданным названием
     * @param name - имя города
     * @return - список объектов городов с заданным названием
     * @throws - CityNotFoundException если объект с таким ID не найден
     */
    List<City> read(String name) throws CityNotFoundException;

    /**
     * Обновляет город с заданным ID,
     * в соответствии с переданным городом
     * @param city - город в соответсвии с которым нужно обновить данные
     * @param id - id города который нужно обновить
     * @return - объект города с заданным ID если данные были обновлены
     * @throws - CityNotFoundException если объект с таким ID не найден
     */
    City update(City city, long id) throws CityNotFoundException;

    /**
     * Переименовывает город с заданным ID,
     * в соответствии с переданным именем
     * @param name - новое название города
     * @param id - id города который нужно обновить
     * @return - объект города с заданным ID если данные были обновлены
     * @throws - CityNotFoundException если объект с таким ID не найден
     */
    City rename(long id, String name) throws CityNotFoundException;

    /**
     * Добавляет совет для города с заданным ID
     * @param advice - новое совет
     * @param id - id города который нужно обновить
     * @return - объект города с заданным ID если данные были обновлены
     * @throws - CityNotFoundException если объект с таким ID не найден
     */
    City addAdvice(long id, String advice) throws CityNotFoundException;

    /**
     * Удаляет совет для города с заданным ID
     * @param index - index совета для удаления
     * @param id - id города который нужно обновить
     * @return - объект города с заданным ID если данные были обновлены
     * @throws - CityNotFoundException если объект с таким ID не найден
     */
    City deleteAdvice(long id, int index) throws CityNotFoundException;

    /**
     * Изменяет совет для города с заданным ID
     * @param index - index совета для удаления
     * @param id - id города который нужно обновить
     * @param advice - новый совет
     * @return - объект города с заданным ID если данные были обновлены
     * @throws - CityNotFoundException если объект с таким ID не найден
     */
    City changeAdvice(long id, int index,String advice) throws CityNotFoundException;

    /**
     * Удаляет города с заданным ID
     * @param id - id города, который нужно удалить
     */
    void delete(long id);

}

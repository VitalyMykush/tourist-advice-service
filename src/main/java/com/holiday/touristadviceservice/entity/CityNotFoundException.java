package com.holiday.touristadviceservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter @Setter
public class CityNotFoundException extends Exception {
    private String cityName;
    private long id;
    public CityNotFoundException(String cityName){
        super("City with name: " + cityName + "  not found");
        this.cityName = cityName;
    }
    public CityNotFoundException(long id){
        super("City with id: " + id + "  not found");
        this.id = id;
    }
    public CityNotFoundException(){
        super("Cities not found");
    }
}

package com.holiday.touristadviceservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CityNotFoundException extends Exception {
    private String cityName;
    private long id;
    public CityNotFoundException(String cityName){
        super("City with name: " + cityName + "  not found");
        this.cityName = cityName;
    }
    public CityNotFoundException(long id){
        super("City with ID: " + id + "  not found");
        this.id = id;
    }
    public CityNotFoundException(){
        super("Cities not found");
    }
}

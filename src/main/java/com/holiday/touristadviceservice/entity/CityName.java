package com.holiday.touristadviceservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
public class CityName {
    @NotBlank
    private String name;

    public CityName(@NotBlank String name) {
        this.name = name;
    }
}

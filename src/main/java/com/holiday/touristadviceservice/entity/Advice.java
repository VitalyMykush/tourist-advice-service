package com.holiday.touristadviceservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Getter @Setter
@NoArgsConstructor
public class Advice {
    @NotBlank
    private String advice;

    public Advice(String advice) {
        this.advice = advice;
    }
}

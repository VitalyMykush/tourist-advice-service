package com.holiday.touristadviceservice.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CITY_ADVICE")
    @OrderColumn
    @Column(name = "ADVICE")
    private List<String> advices = new ArrayList<>();

    public City(String name) {
        this.name = name;
    }

    public void addAdvice(String advice){
        advices.add(advice);
    }
}

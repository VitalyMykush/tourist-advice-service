package com.holiday.touristadviceservice.repository;

import com.holiday.touristadviceservice.entity.City;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends CrudRepository<City,Long> {
    List<City> findByNameIgnoreCase(String name);
}

package com.holiday.touristadviceservice.service;

import com.google.common.collect.Lists;
import com.holiday.touristadviceservice.entity.City;
import com.holiday.touristadviceservice.entity.CityNotFoundException;
import com.holiday.touristadviceservice.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final  CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City create(City city) {
        city.setId(null);
        return cityRepository.save(city);
    }

    @Override
    public List<City> readAll() throws CityNotFoundException{
        List<City> list = Lists.newArrayList(cityRepository.findAll());
        if(!list.isEmpty() && list != null)
            return list;
        else
            throw new CityNotFoundException();
    }

    @Override
    public City read(long id) throws CityNotFoundException {
        return cityRepository.findById(id).orElseThrow(()-> new CityNotFoundException(id));
    }

    @Override
    public List<City> read(String name) throws CityNotFoundException {
        List<City> list = cityRepository.findByNameIgnoreCase(name);
        if(!list.isEmpty() && list != null )
            return list;
        else
            throw new CityNotFoundException(name);
    }

    @Override
    public City update(City city, long id) throws CityNotFoundException {
        City found  = cityRepository.findById(id).orElseThrow(()-> new CityNotFoundException(id));
        city.setId(found.getId());
        return cityRepository.save(city);
    }

    @Override
    public void delete(long id) {
        cityRepository.deleteById(id);
    }


    @Override
    public City rename(long id, String name) throws CityNotFoundException{
        City found= cityRepository.findById(id).orElseThrow(()-> new CityNotFoundException(id));
        found.setName(name);
        return cityRepository.save(found);
    }

    @Override
    public City deleteAdvice(long id, int index) throws IndexOutOfBoundsException,CityNotFoundException{
         City found = cityRepository.findById(id).orElseThrow(()-> new CityNotFoundException(id));
         found.getAdvices().remove(index);
         return cityRepository.save(found);
    }

    @Override
    public City changeAdvice(long id, int index,String advice) throws IndexOutOfBoundsException, CityNotFoundException {
        City city = cityRepository.findById(id).orElseThrow(()-> new CityNotFoundException(id));
        city.getAdvices().set(index, advice);
        return cityRepository.save(city);
    }
    @Override
    public City addAdvice(long id, String advice) throws CityNotFoundException {
        City city = cityRepository.findById(id).orElseThrow(() -> new CityNotFoundException(id));
        city.getAdvices().add(advice);
        return cityRepository.save(city);
    }
}

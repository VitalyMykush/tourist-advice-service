package com.holiday.touristadviceservice.controller;

import com.holiday.touristadviceservice.entity.Advice;
import com.holiday.touristadviceservice.entity.City;
import com.holiday.touristadviceservice.entity.CityName;
import com.holiday.touristadviceservice.entity.CityNotFoundException;
import com.holiday.touristadviceservice.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @ExceptionHandler(CityNotFoundException.class)
    protected ResponseEntity<?> handleCityNotFoundException(CityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IndexOutOfBoundsException.class)
    protected ResponseEntity<?> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCities() throws CityNotFoundException {
        return new ResponseEntity<>(cityService.readAll(), HttpStatus.OK);
    }

    @PostMapping("/cities")
    public ResponseEntity<City> createCity(@RequestBody @Valid City city, BindingResult result) {
        if(result.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<City>(cityService.create(city),HttpStatus.CREATED);
    }

    @PutMapping("/cities/{id}")
    public ResponseEntity<City> updateCity(@PathVariable("id") Long id, @RequestBody @Valid City city, BindingResult result) throws CityNotFoundException {
        if(result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<City>(cityService.update(city,id), HttpStatus.OK);
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCity(@PathVariable("id") Long id) throws CityNotFoundException {
           return new ResponseEntity<City>(cityService.read(id),HttpStatus.OK);
    }

    @DeleteMapping("/cities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCity(@PathVariable("id") Long id) {
        cityService.delete(id);
    }





    @PutMapping("/cities/{id}/edit/name")
    public ResponseEntity<?> renameCity(@PathVariable("id") Long id, @RequestBody @Valid CityName cityName, BindingResult result) throws CityNotFoundException {
        if(result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<City>(cityService.rename(id,cityName.getName()), HttpStatus.OK);
    }

    @PutMapping(value = "/cities/{id}/edit/advices/{index}")
    public ResponseEntity<?> changeAdvice(@PathVariable("id") Long id, @PathVariable("index") int index, @RequestBody @Valid Advice advice, BindingResult result) throws CityNotFoundException,IndexOutOfBoundsException {
        if(result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<City>(cityService.changeAdvice(id,index,advice.getAdvice()),HttpStatus.OK);
    }

    @DeleteMapping(value = "/cities/{id}/edit/advices/{index}")
    public ResponseEntity<?> deleteAdvice(@PathVariable("id") Long id, @PathVariable("index") int index) throws IndexOutOfBoundsException, CityNotFoundException {
            return new ResponseEntity<City>(cityService.deleteAdvice(id,index),HttpStatus.NO_CONTENT);
    }

    @PostMapping("/cities/{id}/edit/advices")
    public ResponseEntity<?> addAdvice(@PathVariable("id") Long id, @RequestBody @Valid Advice advice, BindingResult result) throws CityNotFoundException {
        if(result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<City>(cityService.addAdvice(id,advice.getAdvice()),HttpStatus.CREATED);
    }
}

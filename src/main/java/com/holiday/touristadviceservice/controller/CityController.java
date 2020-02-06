package com.holiday.touristadviceservice.controller;

import com.holiday.touristadviceservice.entity.City;
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
import java.util.Map;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @ExceptionHandler(CityNotFoundException.class)
    protected ResponseEntity<String> handleCityNotFoundException(CityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IndexOutOfBoundsException.class)
    protected ResponseEntity<String> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
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
        return new ResponseEntity<>(cityService.create(city), HttpStatus.CREATED);
    }

    @PutMapping("/cities/{id}")
    public ResponseEntity<City> updateCity(@PathVariable("id") Long id, @RequestBody @Valid City city, BindingResult result) throws CityNotFoundException {
        if(result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cityService.update(city, id), HttpStatus.OK);
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCity(@PathVariable("id") Long id) throws CityNotFoundException {
           return new ResponseEntity<>(cityService.read(id), HttpStatus.OK);
    }

    @DeleteMapping("/cities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCity(@PathVariable("id") Long id) {
        cityService.delete(id);
    }

    @PutMapping("/cities/{id}/edit/name")
    public ResponseEntity<City> renameCity(@PathVariable("id") Long id, @RequestBody Map<String, String> update) throws CityNotFoundException {
        String cityName = update.get("name");
        if(cityName.isEmpty() && cityName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cityService.rename(id, cityName), HttpStatus.OK);
    }

    @PutMapping(value = "/cities/{id}/edit/advices/{index}")
    public ResponseEntity<City> changeAdvice(@PathVariable("id") Long id, @PathVariable("index") int index, @RequestBody Map<String, String> update) throws CityNotFoundException,IndexOutOfBoundsException {
        String advice = update.get("advice");
        if(advice.isEmpty() && advice == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cityService.changeAdvice(id, index, advice), HttpStatus.OK);
    }

    @DeleteMapping(value = "/cities/{id}/edit/advices/{index}")
    public ResponseEntity<City> deleteAdvice(@PathVariable("id") Long id, @PathVariable("index") int index) throws IndexOutOfBoundsException, CityNotFoundException {
            return new ResponseEntity<>(cityService.deleteAdvice(id, index), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/cities/{id}/edit/advices")
    public ResponseEntity<City> addAdvice(@PathVariable("id") Long id, @RequestBody Map<String, String> update) throws CityNotFoundException {
        String advice = update.get("advice");
        if(advice.isEmpty() && advice == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cityService.addAdvice(id, advice), HttpStatus.CREATED);
    }
}

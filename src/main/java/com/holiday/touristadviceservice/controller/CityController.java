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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/cities",produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @ExceptionHandler( CityNotFoundException.class)
    protected ResponseEntity<String> handleCityNotFoundException(CityNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    protected ResponseEntity<String> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<City>> getCities() throws CityNotFoundException {
        return ResponseEntity.ok(cityService.readAll());
    }

    @PostMapping
    public ResponseEntity<String> createCity(@RequestBody @Valid City city, BindingResult result, UriComponentsBuilder uriComponentsBuilder) {
        if(result.hasErrors())
            return ResponseEntity.badRequest().build();

        Long id = cityService.create(city).getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable("id") Long id, @RequestBody @Valid City city, BindingResult result) throws CityNotFoundException {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cityService.update(city,id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCity(@PathVariable("id") Long id) throws CityNotFoundException {
        return ResponseEntity.ok(cityService.read(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCity(@PathVariable("id") Long id) {
        cityService.delete(id);
    }

    @PutMapping("/{id}/edit/name")
    public ResponseEntity<City> renameCity(@PathVariable("id") Long id, @RequestBody Map<String, String> update) throws CityNotFoundException {
        String cityName = update.get("name");
        if(cityName.isEmpty() && cityName == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cityService.rename(id, cityName));
    }

    @PutMapping(value = "/{id}/edit/advices/{index}")
    public ResponseEntity<City> changeAdvice(@PathVariable("id") Long id, @PathVariable("index") int index, @RequestBody Map<String, String> update) throws CityNotFoundException,IndexOutOfBoundsException {
        String advice = update.get("advice");
        if(advice.isEmpty() && advice == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cityService.changeAdvice(id, index, advice));
    }

    @DeleteMapping(value = "/{id}/edit/advices/{index}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdvice(@PathVariable("id") Long id, @PathVariable("index") int index) throws IndexOutOfBoundsException, CityNotFoundException {
            cityService.deleteAdvice(id, index);
    }

    @PostMapping("/{id}/edit/advices")
    public ResponseEntity<City> addAdvice(@PathVariable("id") Long id, @RequestBody Map<String, String> update) throws CityNotFoundException {
        String advice = update.get("advice");
        if(advice.isEmpty() && advice == null) {
            return ResponseEntity.badRequest().build();
        }
        int index = cityService.addAdvice(id,advice).getAdvices().indexOf(advice);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{index}").buildAndExpand(index).toUri();
        return ResponseEntity.created(location).build();
    }
}

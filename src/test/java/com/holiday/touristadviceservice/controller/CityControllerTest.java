package com.holiday.touristadviceservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.holiday.touristadviceservice.entity.City;
import com.holiday.touristadviceservice.entity.CityNotFoundException;
import com.holiday.touristadviceservice.service.CityService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import  static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import  static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @Test
    public void whenGetCityById_thenReturnOkAndCity() throws Exception {
        City minsk = new City("Minsk");
        minsk.setId(1L);
        given(cityService.read(minsk.getId())).willReturn(minsk);

        mockMvc.perform(get("/cities/{id}",minsk.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.name",is(minsk.getName())));
    }

    @Test
    public void whenGetCityById_thenReturnNotFound() throws Exception {
        int id = 0;
        when(cityService.read(id)).thenThrow(new CityNotFoundException(id));

        mockMvc.perform(get("/cities/{id}",id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetCities_thenReturnOkAndCities()throws Exception {

        City minsk = new City("Minsk");
        minsk.setId(1L);

        City brest = new City("Brest");
        brest.setId(2L);

        List<City> cities = Arrays.asList(minsk,brest);
        given(cityService.readAll()).willReturn(cities);

        mockMvc.perform(get("/cities").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id",is(1)))
                .andExpect(jsonPath("$[0].name",is(minsk.getName())))
                .andExpect(jsonPath("$[1].id",is(2)))
                .andExpect(jsonPath("$[1].name",is(brest.getName())));

        verify(cityService,times(1)).readAll();
        verifyNoMoreInteractions(cityService);
    }

    @Test
    public void whenGetCities_thenReturnOkAndEmptyArray() throws Exception {
        List<City> cities = Collections.emptyList();
        given(cityService.readAll()).willReturn(cities);
        mockMvc.perform(get("/cities").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", empty()));

        verify(cityService,times(1)).readAll();
        verifyNoMoreInteractions(cityService);
    }

    @Test
    public void whenPostCity_thenReturnCreatedAndLocation() throws Exception {
        City minsk = new City("Minsk");
        minsk.addAdvice("Good luck");

        City created = new City("Minsk");
        created.setId(1L);
        created.addAdvice("Good luck");

        Map<String, Object> params = new HashMap<>();
        params.put("name", minsk.getName());
        params.put("advices",minsk.getAdvices());
        String jsonCity = new ObjectMapper().writeValueAsString(params);


        given(cityService.create(minsk)).willReturn(created);

        String location = "cities/"+created.getId();

                mockMvc.perform(post("/cities").contentType(MediaType.APPLICATION_JSON).content(jsonCity))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("location", containsString(location)));
        verify(cityService,times(1)).create(minsk);
        verifyNoMoreInteractions(cityService);
    }
    @Test
    public void whenPostNotValidCity_whenReturnBadRequest() throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        String jsonCity = new ObjectMapper().writeValueAsString(params);


        mockMvc.perform(post("/cities").contentType(MediaType.APPLICATION_JSON).content(jsonCity))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(cityService);
    }

    @Test
    public void whenPutCity_thenReturnOkAndUpdatedCity() throws Exception {
        City cityToUpdate = new City("Updated Minsk");
        cityToUpdate.addAdvice("Updated Good luck");

        City updatedCity = new City(cityToUpdate.getName());
        updatedCity.setAdvices(cityToUpdate.getAdvices());
        updatedCity.setId(1L);

        Map<String, Object> params = new HashMap<>();
        params.put("name", cityToUpdate.getName());
        params.put("advices",cityToUpdate.getAdvices());
        String jsonCity = new ObjectMapper().writeValueAsString(params);


        given(cityService.update(cityToUpdate,updatedCity.getId())).willReturn(updatedCity);

        mockMvc.perform(put("/cities/{id}",updatedCity.getId()).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonCity))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(updatedCity.getId())))
                .andExpect(jsonPath("$.name",is(cityToUpdate.getName())))
                .andExpect(jsonPath("$.advices",hasSize(cityToUpdate.getAdvices().size())));

        verify(cityService,times(1)).update(cityToUpdate,updatedCity.getId());
        verifyNoMoreInteractions(cityService);
    }

    @Test
    public void whenPutNotValidCity_thenReturnBadRequest() throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        String jsonCity = new ObjectMapper().writeValueAsString(params);

        mockMvc.perform(put("/cities/{id}",1).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonCity))
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(cityService);
    }

    @Test
    public  void whenPutIdNotExist_thenReturnNotFound() throws Exception {
        City cityToUpdate = new City("Updated Minsk");
        cityToUpdate.addAdvice("Updated Good luck");
        Long id = 666L;

        Map<String, Object> params = new HashMap<>();
        params.put("name", cityToUpdate.getName());
        params.put("advices",cityToUpdate.getAdvices());
        String jsonCity = new ObjectMapper().writeValueAsString(params);

        given(cityService.update(cityToUpdate,id)).willThrow(new CityNotFoundException(id));


        mockMvc.perform(put("/cities/{id}",id).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonCity))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteCityById_thenReturnNoContent() throws Exception {
        Long id = 666L;

        doNothing().when(cityService).delete(id);

        mockMvc.perform(delete("/cities/{id}",id))
                .andExpect(status().isNoContent());
    }




}
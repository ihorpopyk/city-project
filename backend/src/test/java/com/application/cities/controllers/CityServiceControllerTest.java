package com.application.cities.controllers;

import com.application.cities.model.City;
import com.application.cities.services.impl.CityServiceServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityServiceControllerTest {

    MockMvc mvc;

    @InjectMocks
    private CityController cityController;

    @Mock
    private CityServiceServiceImpl service;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final City KYIV_CITY = new City("Kyiv", "http://kyiv.com");
    private static final City TERNOPIL_CITY = new City("Ternopil", "http://ternopil.com");
    private static final List<City> CITIES = List.of(KYIV_CITY, TERNOPIL_CITY);

    private static final City CITY_ONE = new City(1L, "City 1", "http://city1.com");
    private static final City CITY_TWO = new City(2L, "City 2", "http://city2.com");

    @BeforeAll
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(cityController)
                .build();

        Pageable pageable = PageRequest.of(0, 20);
        when(service.findAll(pageable)).thenReturn(new PageImpl<>(CITIES));
        when(service.findByName("kyiv", pageable)).thenReturn(new PageImpl<>(List.of(KYIV_CITY)));
        when(service.update(eq(CITY_ONE))).thenReturn(CITY_ONE);
        when(service.update(eq(CITY_TWO))).thenThrow(new IllegalArgumentException("ERROR"));
    }

    @Test
    void getAllCities() throws Exception {
        mvc.perform(get("/api/v1/cities")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.items", hasSize(2)))
                .andExpect(jsonPath("$.data.items_per_page", equalTo(20)))
                .andExpect(jsonPath("$.data.page_index", equalTo(1)))
                .andExpect(jsonPath("$.data.total_items", equalTo(2)));
    }

    @Test
    void getCitiesByName() throws Exception {
        mvc.perform(get("/api/v1/cities/kyiv")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.items", hasSize(1)))
                .andExpect(jsonPath("$.data.items_per_page", equalTo(20)))
                .andExpect(jsonPath("$.data.page_index", equalTo(1)))
                .andExpect(jsonPath("$.data.total_items", equalTo(1)));
    }

    @Test
    void updateCityShouldWorkAsExpected() throws Exception {
        mvc.perform(put("/api/v1/cities/" + CITY_ONE.getId())
                        .content(MAPPER.writeValueAsString(CITY_ONE))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.id", equalTo(CITY_ONE.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(CITY_ONE.getName())))
                .andExpect(jsonPath("$.photo", equalTo(CITY_ONE.getPhoto())));
    }
}
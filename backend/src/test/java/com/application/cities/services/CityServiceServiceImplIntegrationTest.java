package com.application.cities.services;

import com.application.cities.model.City;
import com.application.cities.repositories.CityRepository;
import com.application.cities.services.impl.CityServiceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityServiceServiceImplIntegrationTest {
    public static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);
    @Autowired
    private CityRepository cityRepository;

    private CityServiceServiceImpl service;

    @BeforeAll
    public void setup() {
        service = new CityServiceServiceImpl(cityRepository);
    }

    @AfterEach
    public void clearDB() {
        cityRepository.deleteAll();
    }

    private void initData() {
        var kyivCity = new City("Kyiv", "http://kyiv.com");
        var ternopilCity = new City("Ternopil", "http://ternopil.com");
        var barcelonaCity = new City("Barcelona", "http://barcelona.com");
        var milanCity = new City("Milan", "http://milan.com");
        var vilnusCity = new City("Vilnus", "http://vilnus.com");

        cityRepository.saveAll(List.of(kyivCity, ternopilCity, barcelonaCity, milanCity, vilnusCity));
    }

    @Test
    void shouldBeEmptyResultWhenNoDataInDB() {
        assertThat(service.findAll(PAGE_REQUEST).getTotalElements())
                .isEqualTo(0);
    }

    @Test
    void shouldBeEmptyResultWhenCityNotPresent() {
        assertThat(service.findByName("QWERTY", PAGE_REQUEST).getTotalElements())
                .isEqualTo(0);
    }

    @Test
    void shouldReturnCitiesWhenDataIsPresent() {
        initData();
        assertThat(service.findAll(PAGE_REQUEST).getTotalElements())
                .isEqualTo(5);
    }


    @Test
    void shouldReturnCityDataWhenDataIsPresent() {
        initData();

        assertThat(service.findByName("Kyiv", PAGE_REQUEST).getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldUpdateCityData() {
        City kyivCity = cityRepository.save(new City("Kyiv", "http://kyiv.com"));
        City kyivCity2 = new City(kyivCity.getId(), "Kyiv2", "http://kyiv2.com");
        service.update(kyivCity2);
        Optional<City> updatedCity = cityRepository.findById(kyivCity2.getId());

        assertThat(updatedCity).isPresent();
        assertAll((() -> {
            assertThat(updatedCity.get().getName()).isEqualTo(kyivCity2.getName());
            assertThat(updatedCity.get().getPhoto()).isEqualTo(kyivCity2.getPhoto());
        }));
    }
}
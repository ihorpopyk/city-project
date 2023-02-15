package com.application.cities.services;

import com.application.cities.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    Page<City> findByName(String name, Pageable pageable);

    Page<City> findAll(Pageable pageable);

    City update(City city);
}

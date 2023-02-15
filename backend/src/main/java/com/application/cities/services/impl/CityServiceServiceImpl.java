package com.application.cities.services.impl;

import com.application.cities.model.City;
import com.application.cities.repositories.CityRepository;
import com.application.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceServiceImpl implements CityService {
    private final CityRepository repository;

    public Page<City> findByName(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<City> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public City update(City city) {
        return repository.save(city);
    }
}

package com.application.cities.controllers;

import com.application.cities.utils.PageableConverter;
import com.application.cities.model.City;
import com.application.cities.services.impl.CityServiceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CityController {
    private final CityServiceServiceImpl service;

    @GetMapping("/cities")
    public ResponseEntity<Map<String, Object>> getCities(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<City> cities = service.findAll(PageRequest.of(page - 1, size));
        return ResponseEntity
                .ok()
                .body(PageableConverter.convert(page, size, cities)
                );
    }

    @GetMapping("/cities/{name}")
    public ResponseEntity<Map<String, Object>> getCity(
            @PathVariable String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var cities = service.findByName(name, PageRequest.of(page - 1, size));
        return ResponseEntity
                .ok()
                .body(PageableConverter.convert(page, size, cities)
                );
    }

    @PutMapping("/cities/{id}")
    public ResponseEntity<?> updateCity(
            @RequestBody City city,
            @PathVariable Long id
    ) {
        return ResponseEntity
                .ok()
                .body(service.update(city));
    }
}

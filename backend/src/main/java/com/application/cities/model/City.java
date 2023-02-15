package com.application.cities.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class City {
    public City(@NonNull String name, @NonNull String photo) {
        this.name = name;
        this.photo = photo;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String photo;
}
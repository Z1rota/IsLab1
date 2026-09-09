package org.zirota.islab1.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.ZonedDateTime;

@Entity

public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Имя не должно быть пустым")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Координаты не могут быть пустыми")
    @ManyToOne
    @JoinColumn(name="coordinates_id", nullable = false)
    private Coordinates coordinates;

    @NotNull
    @Column(name="creation_date",nullable = false)
    private ZonedDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color hairColor;

    @NotNull(message = "Локация не может быть пустой")
    @ManyToOne
    @JoinColumn(name="location_id", nullable = false)
    private Location location;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double height;

    @Enumerated(EnumType.STRING)
    private Country country;


}

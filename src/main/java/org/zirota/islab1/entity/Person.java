package org.zirota.islab1.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Coordinates coordinates;

    private ZonedDateTime creationTime;

    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @ManyToOne
    private Location location;

    private Double height;

    @Enumerated(EnumType.STRING)
    private Country country;


}

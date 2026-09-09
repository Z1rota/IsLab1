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

    @NotNull(message = "Нужен цвет волос")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color hairColor;

    @NotNull(message = "Локация не может быть пустой")
    @ManyToOne
    @JoinColumn(name="location_id", nullable = false)
    private Location location;

    @NotNull
    @Positive(message = "Рост должен быть больше 0")
    @Column(nullable = false)
    private Double height;

    @Enumerated(EnumType.STRING)
    private Country nationality;

    public @NotBlank(message = "Имя не должно быть пустым") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Имя не должно быть пустым") String name) {
        this.name = name;
    }

    public @NotNull(message = "Координаты не могут быть пустыми") Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(@NotNull(message = "Координаты не могут быть пустыми") Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public @NotNull ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@NotNull ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public @NotNull Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(@NotNull Color hairColor) {
        this.hairColor = hairColor;
    }

    public @NotNull(message = "Локация не может быть пустой") Location getLocation() {
        return location;
    }

    public void setLocation(@NotNull(message = "Локация не может быть пустой") Location location) {
        this.location = location;
    }

    public @NotNull @Positive Double getHeight() {
        return height;
    }

    public void setHeight(@NotNull @Positive Double height) {
        this.height = height;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country country) {
        this.nationality = country;
    }
    public Integer getId() {
        return id;
    }
}

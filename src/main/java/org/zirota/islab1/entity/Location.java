package org.zirota.islab1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Координата X не может быть null")
    @Column(nullable = false)
    private Double x;

    private double y;
    private float z;

    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }
    public Float getZ() {
        return z;
    }
    public Long getId() {
        return id;
    }
}

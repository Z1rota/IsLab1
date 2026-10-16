package org.zirota.islab1.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    public void setZ(Float z) {
        this.z = z;
    }

    public Long getId() {
        return id;
    }
}

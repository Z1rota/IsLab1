package org.zirota.islab1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(nullable = false)
    private long x;
    @NotNull
    @Column(nullable = false)
    private long y;


    public long getX() {
        return x;
    }
    public void setX(long x) {
        this.x = x;
    }
    public long getY() {
        return y;
    }
    public void setY(long y) {
        this.y = y;
    }
    public Long getId() {
        return id;
    }
}

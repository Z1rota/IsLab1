package org.zirota.islab1.dto;

import org.zirota.islab1.entity.Country;

public class NationalityCountDto {

    private Country nationality;
    private Long count;

    public NationalityCountDto(Country nationality, Long count) {
        this.nationality = nationality;
        this.count = count;
    }

    public Country getNationality() {
        return nationality;
    }

    public Long getCount() {
        return count;
    }
}

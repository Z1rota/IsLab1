package org.zirota.islab1.dto;

import org.zirota.islab1.entity.Color;
import org.zirota.islab1.entity.Country;

public record PersonImportRow(String name, long coordinateX, long coordinateY, Color EyeColor, Color hairColor,
                              Double locationX, double locationY, float locationZ, Double height, Country nationality) {
}

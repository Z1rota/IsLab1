package org.zirota.islab1.dto;

import org.zirota.islab1.entity.Color;
import org.zirota.islab1.entity.Country;

public record PersonImportRow(String name, long coordinatesX, long coordinatesY, Color eyeColor, Color hairColor,
                              Double locationX, double locationY, float locationZ, Double height, Country nationality) {
}

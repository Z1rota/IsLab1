package org.zirota.islab1.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zirota.islab1.dto.PersonImportRow;
import org.zirota.islab1.entity.Color;
import org.zirota.islab1.entity.Country;
import org.zirota.islab1.exceptions.ImportException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonImportParser {

    public List<PersonImportRow> parse(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImportException("Файл пуст");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

             CSVParser parser = CSVFormat.DEFAULT.builder().setDelimiter(';').
                     setHeader().setSkipHeaderRecord(true).setTrim(true).get().parse(reader)) {

            List<PersonImportRow> rows = new ArrayList<>();

            for (CSVRecord record : parser) {
                rows.add(parseRecord(record));
            }

            if (rows.isEmpty()) {
                throw new ImportException("Файл не содержит объектов");
            }

            return rows;

        } catch (ImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ImportException("Невалидный CSV");
        }
    }

    private PersonImportRow parseRecord(CSVRecord record) {
        try {
            String name = record.get("name");

            if (name == null || name.isBlank()) {
                throw new ImportException("Строка " + record.getRecordNumber() + ": name не может быть пустым");
            }

            double height = Double.parseDouble(record.get("height"));

            if (height <= 0) {
                throw new ImportException("Строка " + record.getRecordNumber() + ": height должен быть больше 0");
            }

            return new PersonImportRow(name, Long.parseLong(record.get("coordinatesX")),
                    Long.parseLong(record.get("coordinatesY")), nullableColor(record.get("eyeColor")),
                    requiredColor(record.get("hairColor"), record.getRecordNumber()),
                    Double.parseDouble(record.get("locationX")), Double.parseDouble(record.get("locationY")),
                    Float.parseFloat(record.get("locationZ")), height, nullableCountry(record.get("nationality")));

        } catch (ImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ImportException("Ошибка в строке " + record.getRecordNumber() + ": " + e.getMessage());
        }
    }

    private Color nullableColor(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Color.valueOf(value.toUpperCase());
    }

    private Color requiredColor(String value, long line) {
        if (value == null || value.isBlank()) {
            throw new ImportException("Строка " + line + ": hairColor обязателен");
        }

        return Color.valueOf(value.toUpperCase());
    }

    private Country nullableCountry(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Country.valueOf(value.toUpperCase());
    }
}
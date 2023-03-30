package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class StringToLocalDateConverter implements Converter<String, LocalDate> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convert(String dateString) {
        return LocalDate.parse(dateString, DATE_TIME_FORMATTER_DATE);
    }
}

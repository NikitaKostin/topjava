package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Override
    public LocalTime convert(String timeString) {
        return LocalTime.parse(timeString, DATE_TIME_FORMATTER_TIME);
    }
}

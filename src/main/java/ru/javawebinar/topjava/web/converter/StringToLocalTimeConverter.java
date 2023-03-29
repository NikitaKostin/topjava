package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.getDateTimeFormatterTime;

public final class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String timeString) {
        return LocalTime.parse(timeString, getDateTimeFormatterTime());
    }
}

package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

import static ru.javawebinar.topjava.util.DateTimeUtil.getDateTimeFormatterDate;

public final class StringToLocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String dateString) {
        return LocalDate.parse(dateString, getDateTimeFormatterDate());
    }
}

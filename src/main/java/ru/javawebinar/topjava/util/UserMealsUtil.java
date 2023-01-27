package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcess = new ArrayList<>();
        HashMap<Long, Integer> caloriesMap = new HashMap<>();

        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                long second = meal.getDateTime().toLocalDate().toEpochDay();
                int caloriesTotal = caloriesMap.getOrDefault(second, 0);
                caloriesMap.put(second, caloriesTotal + meal.getCalories());
            }
        }

        for (UserMeal meal : meals) {
            LocalTime currentTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(currentTime, startTime, endTime)) {
                long second = meal.getDateTime().toLocalDate().toEpochDay();
                userMealWithExcess.add(
                        new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                caloriesMap.getOrDefault(second, 0) > caloriesPerDay)
                );
            }
        }

        return userMealWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        HashMap<Long, Integer> caloriesMap = new HashMap<>();
        List<UserMeal> sortedUserMeal = meals
                .stream()
                .filter(userMeal -> {
                    LocalTime currentTime = userMeal.getDateTime().toLocalTime();
                    return TimeUtil.isBetweenHalfOpen(currentTime, startTime, endTime);
                })
                .collect(Collectors.toList());

        sortedUserMeal.forEach(userMeal -> {
            long second = userMeal.getDateTime().toLocalDate().toEpochDay();
            int caloriesTotal = caloriesMap.getOrDefault(second, 0);
            caloriesMap.put(second, caloriesTotal + userMeal.getCalories());
        });
        return sortedUserMeal
                .stream()
                .map(userMeal -> {
                    long second = userMeal.getDateTime().toLocalDate().toEpochDay();
                    return new UserMealWithExcess(
                            userMeal.getDateTime(),
                            userMeal.getDescription(),
                            userMeal.getCalories(),
                            caloriesMap.getOrDefault(second, 0) > caloriesPerDay);
                })
                .collect(Collectors.toList());
    }
}

package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак второго пользователя", 500), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());

            repository.computeIfPresent(userId, (integer, mealMap) -> {
                mealMap.put(meal.getId(), meal);
                return mealMap;
            });

            repository.computeIfAbsent(userId, integer -> new HashMap<Integer, Meal>() {{
                put(meal.getId(), meal);
            }});
            return meal;
        }

        if (!repository.containsKey(userId)
                || !repository.get(userId).containsKey(meal.getId())
        ) {
            return null;
        }

        // handle case: update, but not present in storage
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null
                ? meals.getOrDefault(id, null)
                : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getOrDefault(userId, new HashMap<>())
                .values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllFilteredByDateTime(int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return getAll(userId)
                .stream()
                .filter(meal -> startDate == null && endDate == null || DateTimeUtil.isBetweenClosed(meal.getDate(), startDate, endDate))
                .filter(meal -> startTime == null && endTime == null || DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
    }
}


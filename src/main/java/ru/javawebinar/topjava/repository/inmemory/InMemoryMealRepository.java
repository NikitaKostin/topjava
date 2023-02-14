package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserid(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }

        if (!meal.getUserid().equals(userId) || !repository.get(meal.getId()).getUserid().equals(userId)) {
            return null;
        }

        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserid().equals(userId) && repository.containsKey(id) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserid().equals(userId)
                ? meal
                : null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserid().equals(userId))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}


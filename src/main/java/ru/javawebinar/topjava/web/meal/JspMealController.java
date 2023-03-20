package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.RootController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/topjava/meals")
public class JspMealController extends AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    @PostMapping("/{id}/update")
    public String updateMeal(Model model, @PathVariable String id) {
        log.info("updateMeal {}", id);
        model.addAttribute("meal", super.get(Integer.parseInt(id)));
        return "mealForm";
    }

    @PostMapping("/{id}/delete")
    public String deleteMeal(@PathVariable String id) {
        log.info("deleteMeal {}", id);
        super.delete(Integer.parseInt(id));
        return "redirect:/topjava/meals";
    }

    @PostMapping("/create")
    public String createMeal(Model model) {
        log.info("createMeal");
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @PostMapping(value = "/save")
    public String saveMeal(HttpServletRequest request) {
        log.info("saveMeal");
        super.create(fillMeal(request));
        return "redirect:/topjava/meals";
    }

    @PostMapping(value = "{id}/save")
    public String updateMealWithId(HttpServletRequest request, @PathVariable() Integer id) {
        log.info("updateMealWithId {}", id);
        super.update(fillMeal(request), id);
        return "redirect:/topjava/meals";
    }

    @GetMapping
    public String getMeals(HttpServletRequest request, Model model) {
        String action = request.getParameter("action");
        if ("filter".equals(action)) {
            log.info("meals with filter");
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        } else {
            log.info("meals");
            model.addAttribute("meals", super.getAll());
        }
        return "meals";
    }

    private Meal fillMeal(HttpServletRequest request) {
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        return new Meal(dateTime, description, calories);
    }
}

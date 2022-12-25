package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validator {

    public static boolean filmValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Error adding a film! The title cannot be empty!");
            throw new ValidationException("Unable to add a film, the title cannot be empty!");
        }

        if (film.getDescription() != null && film.getDescription().length() >200) {
            log.info("Error adding a film! Exceeded the maximum description length of 200!");
            throw new ValidationException("Unable to add a film! Exceeded the maximum description length of 200");
        }

        if(film.getDuration() < 0) {
            log.info("Error adding a film {}. The duration of the film should be positive!",
                    film.getName(), film.getReleaseDate());
            throw new ValidationException("The duration of the film should be positive!");
        }
        return true;
    }

    public static boolean userValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Invalid email address {} User {} (id {})", user.getEmail(), user.getName(),
                    user.getId());
            throw new ValidationException("Invalid email address " + user.getEmail());
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Invalid login {} User {} (id {})", user.getLogin(), user.getName(), user.getId());
            throw new ValidationException("The login must be filled in and must not contain spaces!");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Invalid date of birthday {} User {} (id {})", user.getBirthday(),
                    user.getName(), user.getId());
            throw new ValidationException("Invalid date of birthday!");
        }

        return true;
    }
}

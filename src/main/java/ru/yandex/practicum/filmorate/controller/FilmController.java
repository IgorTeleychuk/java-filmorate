package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    protected static int nextFilmId = 0;

    @PostMapping("/films")
    public Film addNewFilm( @RequestBody Film film) throws ValidationException {
        if (filmValidation(film)) {
            film.setId(getNextFilmId());
            films.put(film.getId(), film);
            log.info("A new film has been added");
            return film;
        } else {
            log.info("The film failed validation");
            throw new ValidationException("Film was not added");
        }
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (filmValidation(film)) {
            if (!(films.keySet().contains(film.getId()))) {
                throw new ValidationException("Film unknown");
            }
            films.put(film.getId(), film);
            log.info("Film was updated");
            return film;
        } else {
            log.info("The film failed validation");
            throw new ValidationException("Film was not updated");
        }
    }

    @GetMapping("/films")
    public Collection<Film> returnFilms() {
        Collection<Film> filmList = films.values();
        log.info("Received a request for a list of movies");
        return filmList;
    }

    private boolean filmValidation (Film film) throws ValidationException {
        boolean b = true;
        if  (film.getName() == null || film.getName().isBlank()) {
            b = false;
            throw new ValidationException("The name cannot be empty");
        }
        if (film.getDescription().length() > 200) {
            b = false;
            throw new ValidationException("The maximum length of the description is 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            b = false;
            throw new ValidationException("Release date — no earlier than December 28, 1895");
        }
        if (film.getDuration() <= 0) {
            b = false;
            throw new ValidationException("The duration of the film should be positive");
        }
        return b;
    }

    public static int getNextFilmId() {
        return ++nextFilmId;
    }
}
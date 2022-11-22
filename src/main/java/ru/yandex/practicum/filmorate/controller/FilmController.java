package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    protected static int nextFilmId = 0;
    private final LocalDate earlyDate = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        filmValidation(film);
        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        log.info("A new film has been added");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmValidation(film);
        if (!(films.keySet().contains(film.getId()))) {
            throw new ValidationException("Film unknown");
        }
        films.put(film.getId(), film);
        log.info("Film was updated");
        return film;
    }

    @GetMapping
    public Collection<Film> returnFilms() {
        Collection<Film> filmList = films.values();
        log.info("Received a request for a list of movies");
        return filmList;
    }

    private boolean filmValidation (Film film) {
        boolean b = true;
        if (film.getReleaseDate().isBefore(earlyDate)) {
            log.info("The film failed validation");
            b = false;
            throw new ValidationException("Release date — no earlier than December 28, 1895");
        }
        return b;
    }

    public static int getNextFilmId() {
        return ++nextFilmId;
    }
}
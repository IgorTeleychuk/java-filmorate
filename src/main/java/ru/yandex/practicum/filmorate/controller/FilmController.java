package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceInterface;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmServiceInterface filmService;

    @GetMapping
    public List<Film> returnAllFilms() {
        log.info("Request GET /films received");
        return filmService.getListFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Request GET /films/{id} received");
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Request GET /films/popular received");
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addNewFilm(@Valid @RequestBody Film film) {
        log.info("Request POST /films received");
        Film addFilm = filmService.addFilm(film);
        log.info("A new film has been added");
        return addFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Request PUT /films received");
        Film updateFilm = filmService.updateFilm(film);
        log.info("Film was updated");
        return updateFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Request PUT /films/{id}/like/{userId} received");
        Film film = filmService.addLike(id,userId);
        log.info("Like added!");
        return film;
    }

    @DeleteMapping
    public Film delete(@Valid @RequestBody Film film) {
        log.info("Request DELETE /films received");
        Film removeFilm = filmService.removeFilm(film);
        log.info("Film deleted successful");
        return  removeFilm;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Request DELETE /films/{id}/like/{userId} received");
        Film film = filmService.removeLike(id, userId);
        log.info("FLike was deleted");
        return film;
    }
}
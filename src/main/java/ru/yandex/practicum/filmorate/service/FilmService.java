package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmService {
    Film addNew(Film film);

    Film update(Film film);

    Film remove(Film film);

    Optional<Film> getById(Integer id);

    List<Film> getList();

    Film addLike(Integer id, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getPopular(Integer count);

    Map<Integer, Film> findFilm();

    LocalDate getEarlyDate();
}

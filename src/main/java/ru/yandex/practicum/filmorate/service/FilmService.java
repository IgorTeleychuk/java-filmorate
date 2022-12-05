package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addNew(Film film);

    Film update(Film film);

    Film remove(Integer id);

    Film getById(Integer id);

    List<Film> getList();

    Film addLike(Integer id, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getPopular(Integer count);

}

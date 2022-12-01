package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceInterface {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film removeFilm(Film film);

    Film getFilm(Integer id);

    List<Film> getListFilms();

    Film addLike(Integer id, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(Integer count);

}

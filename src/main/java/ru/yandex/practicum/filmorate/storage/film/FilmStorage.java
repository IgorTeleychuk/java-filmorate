package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addNewFilm(Film film);

    Film updateFilm(Film film);

    Film removeFilm(Film film);

    Film getFilm(Integer id);

    List<Film> getListFilms();
}

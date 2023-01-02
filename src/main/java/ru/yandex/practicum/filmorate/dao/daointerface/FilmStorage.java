package ru.yandex.practicum.filmorate.dao.daointerface;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addNew(Film film);

    Film update(Film film);

    void remove(Film film);

    List<Film> getById(Integer id);

    List<Film> getList();

    void deleteGenres(int filmID);

    List<Film> getPopularFilm(int count);
}

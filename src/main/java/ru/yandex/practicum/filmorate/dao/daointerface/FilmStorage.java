package ru.yandex.practicum.filmorate.dao.daointerface;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addNew(Film film);

    Film update(Film film);

    void remove(Film film);

    List<Film> getById(Integer id);

    List<Film> getList();

    void deleteGenres(int filmID);

    void addGenre(int filmId, int genreId);

    List<Film> getPopularFilm(int count);
}

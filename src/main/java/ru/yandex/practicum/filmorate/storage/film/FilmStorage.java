package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    Film addNew(Film film);

    Film update(Film film);

    Film remove(Film film);

    Optional<Film> getById(Integer id);

    List<Film> getList();

    Map<Integer, Film> findFilm();

    LocalDate getEarlyDate();
}

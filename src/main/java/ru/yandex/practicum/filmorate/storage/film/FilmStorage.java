package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addNew(Film film);

    Film update(Film film);

    Film remove(Integer id);

    Optional<Film> getById(Integer id);

    List<Film> getList();
}

package ru.yandex.practicum.filmorate.service.serviceinterface;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    Film addNew(Film film);

    Film update(Film film);

    void remove(Film film);

    Film getById(Integer id);

    List<Film> getList();

    void addLike(Integer id, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> getPopular(int count);

}

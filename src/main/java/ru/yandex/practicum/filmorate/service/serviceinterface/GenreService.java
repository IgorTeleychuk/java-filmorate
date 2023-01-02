package ru.yandex.practicum.filmorate.service.serviceinterface;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    Genre getGenreById(int id);
}

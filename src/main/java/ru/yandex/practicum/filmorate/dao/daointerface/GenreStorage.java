package ru.yandex.practicum.filmorate.dao.daointerface;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> getGenreByID(int id);
}

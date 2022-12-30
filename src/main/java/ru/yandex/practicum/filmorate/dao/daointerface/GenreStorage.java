package ru.yandex.practicum.filmorate.dao.daointerface;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> getGenreById(int id);


}

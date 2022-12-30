package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.daointerface.FilmStorage;
import ru.yandex.practicum.filmorate.dao.daointerface.GenreStorage;
import ru.yandex.practicum.filmorate.dao.daointerface.LikesStorage;
import ru.yandex.practicum.filmorate.service.serviceinterface.FilmService;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;

    @Override
    public Film addNew(Film film) {
        return filmStorage.addNew(film);
    }

    @Override
    public Film update(Film film) {
        getById(film.getId());
        return filmStorage.update(film);
    }

    @Override
    public void remove(Film film) {
        filmStorage.remove(film);
    }

    @Override
    public Film getById(Integer id) {
        return filmStorage.getById(id).orElseThrow(() -> new UserNotFoundException("Film with " +
                id + " not found."));
    }

    @Override
    public List<Film> getList() {
        return filmStorage.getList();
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        getById(filmId);
        likesStorage.addLike(filmId, userId);

    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        getById(filmId);
        likesStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmStorage.getPopularFilm(count);
    }
}

package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addNew(Film film) {
            return filmStorage.addNew(film);
    }

    @Override
    public Film update(Film film) {
        if (filmStorage.getById(film.getId()).isPresent()) {
            return filmStorage.update(film);
        } else {
            throw new FilmNotFoundException("The film was not found");
        }
    }

    @Override
    public Film remove(Integer id) {
        if (filmStorage.getById(id).isPresent()) {
            return filmStorage.remove(id);
        } else {
            throw new FilmNotFoundException("The film was not found");
        }
    }

    @Override
    public Film getById(Integer id) {
        return filmStorage.getById(id).orElseThrow(() ->
                new FilmNotFoundException("The film was not found"));
    }

    @Override
    public List<Film> getList() {
        return filmStorage.getList();
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        if (filmStorage.getById(filmId).isPresent() && userStorage.getById(userId).isPresent()) {
            Film film = filmStorage.getById(filmId).get();
            film.getLikes().add(userId);
            return filmStorage.update(film);
        } else {
            throw new FilmNotFoundException("Film or User was not found");
        }

    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {
        if (filmStorage.getById(filmId).isPresent() && userStorage.getById(userId).isPresent()) {
        Film film = filmStorage.getById(filmId).get();

        film.getLikes().remove(userId);
        return filmStorage.update(film);
        } else {
            throw new FilmNotFoundException("Film or User was not found");
        }
    }

    @Override
    public List<Film> getPopular(Integer count) {
        List<Film> films = filmStorage.getList();
        return films
                .stream()
                .sorted((f1,f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}

package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        return filmStorage.update(film);
    }

    @Override
    public Film remove(Film film) {
        return filmStorage.remove(film);
    }

    @Override
    public Optional<Film> getById(Integer id) {
        return Optional.ofNullable(filmStorage.getById(id).orElseThrow(() ->
                new FilmNotFoundException("The film was not found")));
    }

    @Override
    public List<Film> getList() {
        return filmStorage.getList();
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getById(filmId).get();
        User user = userStorage.getById(userId).get();

        if (film == null) {
            throw new FilmNotFoundException("The film was not found");
        }

        if (user == null) {
            throw new UserNotFoundException("The user was not found");
        }

        film.getLikes().add(userId);

        return filmStorage.update(film);
    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getById(filmId).get();
        User user = userStorage.getById(userId).get();

        if (film == null) {
            throw new FilmNotFoundException("The film was not found");
        }

        if (user == null) {
            throw new UserNotFoundException("The user was not found");
        }

        film.getLikes().remove(userId);

        return filmStorage.update(film);
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

    @Override
    public Map<Integer, Film> findFilm() {
        return filmStorage.findFilm();
    }

    @Override
    public LocalDate getEarlyDate() {
        return filmStorage.getEarlyDate();
    }
}

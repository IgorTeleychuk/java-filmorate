package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmServiceInterface {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addNewFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film removeFilm(Film film) {
        return filmStorage.removeFilm(film);
    }

    @Override
    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public List<Film> getListFilms() {
        return filmStorage.getListFilms();
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);

        if (film == null) {
            throw new FilmNotFoundException("The film was not found");
        }

        if (user == null) {
            throw new UserNotFoundException("The user was not found");
        }

        film.getLikes().add(userId);

        return filmStorage.updateFilm(film);
    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);

        if (film == null) {
            throw new FilmNotFoundException("The film was not found");
        }

        if (user == null) {
            throw new UserNotFoundException("The user was not found");
        }

        film.getLikes().remove(userId);

        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = filmStorage.getListFilms();
        return films
                .stream()
                .sorted((f1,f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}

package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    protected static int nextFilmId = 0;
    private static final LocalDate earlyDate = LocalDate.of(1895, 12, 28);

    @Override
    public Film addNew(Film film) {
        nextFilmId++;
        film.setId(nextFilmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        film.setId(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film remove(Film film) {
        Film removeFilm = films.get(film.getId());
        films.remove(film.getId());
        return removeFilm;
    }

    @Override
    public Optional<Film> getById(Integer id) {
            return Optional.of(films.get(id));
    }

    @Override
    public List<Film> getList() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Map<Integer, Film> findFilm() {
        return films;
    }

    @Override
    public LocalDate getEarlyDate() {
        return earlyDate;
    }
}

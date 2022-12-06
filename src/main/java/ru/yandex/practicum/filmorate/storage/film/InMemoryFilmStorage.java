package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    protected static int nextFilmId = 0;

    @Override
    public Film addNew(Film film) {
        nextFilmId++;
        film.setId(nextFilmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film remove(Integer id) {
        Film removeFilm = films.get(id);
        films.remove(id);
        return removeFilm;
    }

    @Override
    public Optional<Film> getById(Integer id) {
            return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getList() {
        return new ArrayList<>(films.values());
    }
}

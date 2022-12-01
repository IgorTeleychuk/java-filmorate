package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    protected static int nextFilmId = 0;
    private final LocalDate earlyDate = LocalDate.of(1895, 12, 28);

    @Override
    public Film addNewFilm(Film film) {
        if(!film.getReleaseDate().isBefore(earlyDate)){
            nextFilmId++;
            film.setId(nextFilmId);
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Release date — no earlier than December 28, 1895");
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if(films.containsKey(film.getId())){
            film.setId(film.getId());
            films.put(film.getId(), film);
        } else {
            throw new FilmNotFoundException("The film was not found");
        }
        return film;
    }

    @Override
    public Film removeFilm(Film film) {
        if(!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("The film was not found");
        } else {
            Film removeFilm = films.get(film.getId());
            films.remove(film.getId());
            return removeFilm;
        }
    }

    @Override
    public Film getFilm(Integer id) {
        if(films.containsKey(id)){
            return films.get(id);
        } else {
            throw new FilmNotFoundException("The film was not found");
        }
    }

    @Override
    public List<Film> getListFilms() {
        return new ArrayList<>(films.values());
    }
}

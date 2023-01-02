package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.service.serviceinterface.FilmService;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DBFilmTest {

    private final FilmService filmService;

    private Mpa mpa;
    private LinkedHashSet<Genre> genre;
    private Film film;


    @BeforeEach
    public void createFilm() {
        mpa = new Mpa(2, "PG");
        genre = new LinkedHashSet<>();
        genre.add(new Genre(1, "Комедия"));
        film = new Film();
        film.setName("Back to the Future");
        film.setDescription("Genre: Science fiction, Comedy, Adventure");
        film.setReleaseDate(LocalDate.of(1985, Month.JULY, 3));
        film.setDuration(116);
        film.setMpa(mpa);
        film.setGenres(genre);

    }

    @Test
    public void testCreateFilm() {

        Optional<Film> filmOptional = Optional.ofNullable(filmService.addNew(film));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testUpdateFilm() {

        filmService.addNew(film);
        film.setId(1);
        film.setDescription("An adventure fantasy film");
        filmService.update(film);
        assertEquals(film.getDescription(), "An adventure fantasy film");
    }

    @Test
    public void testDeleteFilm() {

        filmService.addNew(film);
        film.setId(1);
        filmService.remove(film);
        assertThrows(FilmNotFoundException.class, () -> filmService.getById(1), "Film was found");


    }

    @Test
    public void testGetFilmByID() {
        filmService.addNew(film);
        film.setId(1);
        assertEquals(film, filmService.getById(1));
    }

    @Test
    public void testFindAllFilm() {
        filmService.addNew(film);
        film.setId(1);
        List<Film> films = filmService.getList();
        List<Film> films2 = new ArrayList<>();
        films2.add(film);
        assertTrue(films.equals(films2), "The list with all the movies is not equal to the expected one");
    }

    @Test
    public void testGetPopularFilm() {
        filmService.addNew(film);
        film.setId(1);
        Film film2 = new Film();
        film2.setName("Back to the Future");
        film2.setDescription("Genre: Science fiction, Comedy, Adventure");
        film2.setReleaseDate(LocalDate.of(1985, Month.JULY, 3));
        film2.setDuration(116);
        film2.setMpa(mpa);
        film2.setGenres(genre);
        filmService.addNew(film2);
        film2.setId(2);
        List<Film> popular = new ArrayList<>();
        popular.add(film);
        assertTrue(popular.equals(filmService.getPopular(1)),
                "The list with all the movies is not equal to the expected one");
    }
}
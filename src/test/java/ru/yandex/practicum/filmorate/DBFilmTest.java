package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DBFilmTest {

    private final FilmDbStorage filmStorage;

    private Mpa mpa;
    private Set<Genre> genre;
    private Film film;


    @BeforeEach
    public void createFilm() {
        mpa = new Mpa(2, "PG");
        genre = new HashSet<>();
        genre.add(new Genre(1, "Comedy"));
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

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addNew(film));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testUpdateFilm() {

        filmStorage.addNew(film);
        film.setId(1);
        film.setDescription("An adventure fantasy film");
        filmStorage.update(film);
        Optional<Film> filmOptional = filmStorage.getById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description",
                                "An adventure fantasy film")
                );
    }

    @Test
    public void testDeleteFilm() {

        filmStorage.addNew(film);
        film.setId(1);
        filmStorage.remove(film);
        Optional<Film> filmOptional = filmStorage.getById(1);
        assertThat(filmOptional).isEmpty();
    }

    @Test
    public void testGetFilmByID() {
        filmStorage.addNew(film);
        film.setId(1);
        Optional<Film> filmOptional = filmStorage.getById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllFilm() {
        filmStorage.addNew(film);
        film.setId(1);
        List<Film> films = filmStorage.getList();
        List<Film> films2 = new ArrayList<>();
        films2.add(film);
        assertTrue(films.equals(films2), "The list with all the movies is not equal to the expected one");
    }

    @Test
    public void testGetPopularFilm() {
        filmStorage.addNew(film);
        film.setId(1);
        Film film2 = new Film();
        film2.setName("Back to the Future");
        film2.setDescription("Genre: Science fiction, Comedy, Adventure");
        film2.setReleaseDate(LocalDate.of(1985, Month.JULY, 3));
        film2.setDuration(116);
        film2.setMpa(mpa);
        film2.setGenres(genre);
        filmStorage.addNew(film2);
        film2.setId(2);
        List<Film> popular = new ArrayList<>();
        popular.add(film);
        assertTrue(popular.equals(filmStorage.getPopularFilm(1)),
                "The list with all the movies is not equal to the expected one");
    }
}
package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.daointerface.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class GenreDao implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs, rowNum));
    }

    public Optional<Genre> getGenreById(int id) {
        String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        List<Genre> genreRows = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs, rowNum), id);
        if (genreRows.size() > 0) {
            Genre genre = genreRows.get(0);
            log.info("Genre found by id: {} {}", genre.getName(), id);
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void loadGenreToFilm(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        final String sqlQuery = "select * from GENRES as g, FILM_GENRES as fg where fg.genre_id = g.genre_id and fg.film_id " +
                "in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("FILM_ID"));
            film.getGenres().add(makeGenre(rs, 0));
        }, films.stream().map(Film::getId).toArray());
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}

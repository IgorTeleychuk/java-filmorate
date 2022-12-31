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
    public void makeFilmGenres(List<Film> list) {
        String sqlQuery = "SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID=?";
        LinkedHashSet<Genre> set = new LinkedHashSet<>();

        for (Film film: list) {
            set.clear();
            for (Integer integer : jdbcTemplate.queryForList(sqlQuery, Integer.class, film.getId())) {
                Genre genreById = jdbcTemplate.queryForObject("SELECT * FROM GENRES where GENRE_ID = ?",
                        (rs, rowNum) -> {
                            Genre newGenre = new Genre(rs.getInt("GENRE_ID"),
                                    rs.getString("GENRE_NAME"));
                            return newGenre;
                        }, integer);
                set.add(genreById);
            }
            film.setGenres(set);
        }
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}

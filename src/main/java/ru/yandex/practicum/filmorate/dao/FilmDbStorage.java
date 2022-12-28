package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.daointerface.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository()
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    @Override
    public List<Film> getList() {
        String sqlQuery = "select * from FILMS as f inner join MOTION_PICTURE_ASSOCIATIONS as mpa on f.MPA_ID = mpa.MPA_ID";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs, rowNum));
    }

    @Override
    public Film addNew(Film film) {
        String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, (int) film.getDuration());
            stmt.setObject(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        film.getGenres().forEach(genre -> addGenre(film.getId(), genre.getId()));
        film.setGenres(new HashSet<>(getGenres(film.getId())));
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update FILMS set " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ? , DURATION = ? , MPA_ID = ?" +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteGenres(film.getId());
        film.getGenres().forEach(genre -> addGenre(film.getId(), genre.getId()));
        film.setGenres(new HashSet<>(getGenres(film.getId())));
        return film;
    }

    @Override
    public void remove(Film film) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        if (jdbcTemplate.update(sqlQuery, film.getId()) > 0) {
            log.info("Film with ID {} was remove", film.getId());
        } else throw new RuntimeException("Couldn't delete film from id " + film.getId());
    }

    @Override
    public Optional<Film> getById(Integer id) {
        String sqlQuery = "select * from FILMS as f inner join MOTION_PICTURE_ASSOCIATIONS as mpa on f.MPA_ID = mpa.MPA_ID where FILM_ID = ?";
        List<Film> filmRows = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs, rowNum), id);
        if (filmRows.size() > 0) {
            Film film = filmRows.get(0);
            log.info("Found a film: id: {}, name: {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getGenres(int filmID) {
        String sqlQuery = "select g.GENRE_ID AS GENRE_ID, g.GENRE_NAME from FILM_GENRES AS fg " +
                "LEFT OUTER JOIN GENRES AS g ON fg.GENRE_ID = g.GENRE_ID " +
                "WHERE FILM_ID = ?" + "ORDER BY GENRE_ID ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> genreDao.getGenreById(
                rs.getInt("GENRE_ID")).get(), filmID);
    }

    @Override
    public void deleteGenres(int filmID) {
        String sqlQuery = "delete from FILM_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmID);
        log.info("Genres of the film with ID {} was remove", filmID);
    }

    @Override
    public void addGenre(int filmId, int genreId) {
        String sqlQuery = "merge into FILM_GENRES(FILM_ID, GENRE_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public List<Film> getPopularFilm(int count) {
        StringBuilder str = new StringBuilder();

        str.append("SELECT * ")
                .append("FROM FILMS AS f ")
                .append("INNER JOIN MOTION_PICTURE_ASSOCIATIONS as mpa on f.MPA_ID = mpa.MPA_ID ")
                .append("LEFT OUTER JOIN LIKES AS l ON f.FILM_ID = l.FILM_ID ")
                .append("GROUP BY f.FILM_ID ")
                .append("ORDER BY COUNT(l.USER_ID) DESC ")
                .append("LIMIT ? ");

        String sqlQuery = str.toString();
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs, rowNum), count);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("DESCRIPTION");
        Date releaseDate = rs.getDate("RELEASE_DATE");
        int duration = rs.getInt("DURATION");
        int mpa_id = rs.getInt("MPA_ID");
        String mpa_name = rs.getString("MPA_NAME");
        Mpa mpa = new Mpa(mpa_id, mpa_name);
        LocalDate filmRelease = null;
        if (releaseDate != null) {
            filmRelease = releaseDate.toLocalDate();
        }
        Film film = new Film(id, name, description, filmRelease, duration, mpa, new HashSet<>(getGenres(id)));
        return film;
    }
}

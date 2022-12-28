package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.daointerface.LikesStorage;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;


@Repository
@Slf4j
public class LikesDao implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int filmId, long userId) {
        String sqlQuery = "merge into LIKES(FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void deleteLike(int filmId, long userId) {
        String sqlQuery = "delete from LIKES where FILM_ID = ? AND USER_ID = ?";
        if (jdbcTemplate.update(sqlQuery, filmId, userId) == 0) {
            throw new LikeNotFoundException("Couldn't delete user's like with id " + userId +
                    " to the film with id" + filmId);
        }
    }
}

package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.daointerface.LikesStorage;


@Component
public class LikesDao implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(LikesDao.class);

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
            throw new RuntimeException("Couldn't delete user's like with id " + userId +
                    " to the film with id" + filmId);
        }
    }
}

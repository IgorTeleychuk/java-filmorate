package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.daointerface.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component()
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getList() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs, rowNum));
    }

    @Override
    public User addNew (User user) {
        String sqlQuery = "insert into USERS(USER_NAME, EMAIL, LOGIN, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update USERS set " +
                "USER_NAME = ?, EMAIL = ?, LOGIN = ? , BIRTHDAY = ?" +
                "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void remove(User user) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        if(jdbcTemplate.update(sqlQuery, user.getId()) >0) {
            log.info("User with ID {} was remove", user.getId());
        } else {
            throw new RuntimeException("Couldn't delete user with id " + user.getId());
        }
    }

    @Override
    public Optional<User> getById(Integer id) {

        String sqlQuery = "select * from USERS where USER_ID = ?";
        List<User> userRows = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs, rowNum), id);
        if(userRows.size() > 0) {
            User user = userRows.get(0);
            log.info("User found: {} {}", user.getId(), user.getLogin());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getFriends(Integer userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select u.USER_ID, ")
                .append("u.USER_NAME, ")
                .append("u.EMAIL, ")
                .append("u.LOGIN, ")
                .append("u.BIRTHDAY ")
                .append("from FRIENDS AS f ")
                .append("LEFT OUTER JOIN USERS AS u ON f.FRIEND_ID = u.USER_ID ")
                .append("where f.USER_ID = ?");
        String sqlQuery = sb.toString();
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs, rowNum), userId);
    }

    @Override
    public List<User> commonFriends(Integer userId, Integer friendId) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT u.USER_ID,")
                .append("u.EMAIL,")
                .append("u.LOGIN,")
                .append("u.USER_NAME,")
                .append("u.BIRTHDAY ")
                .append("FROM FRIENDS AS fr1 ")
                .append("INNER JOIN FRIENDS AS fr2 ON fr1.FRIEND_ID = fr2.FRIEND_ID ")
                .append("LEFT OUTER JOIN USERS u ON fr1.FRIEND_ID = u.USER_ID ")
                .append("WHERE fr1.USER_ID = ? AND fr2.USER_ID = ? ");
        //для подтвержденной дружбы
        //.append("AND fr1.confirmed IS TRUE AND fr2.confirmed IS TRUE");

        String sqlQuery = str.toString();
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs, rowNum), userId, friendId);
    }
    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        Integer id = resultSet.getInt("USER_ID");
        String email = resultSet.getString("EMAIL");
        String login = resultSet.getString("LOGIN");
        String name = resultSet.getString("USER_NAME");
        Date birthday = resultSet.getDate("BIRTHDAY");
        LocalDate userBirthday = null;
        if(birthday != null) {
            userBirthday = birthday.toLocalDate();
        }
        return new User(id, email, login, name, userBirthday);
    }

}

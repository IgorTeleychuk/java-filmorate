package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.daointerface.FriendsStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class FriendsDao implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "merge into FRIENDS(USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        if(jdbcTemplate.update(sqlQuery, userId, friendId) > 0) {
            if (checkFriendshipStatus(userId, friendId)) {
                addFriend(friendId, userId);
            }
        } else {
            throw new RuntimeException("Failed to delete a friend with id " + friendId + "user with an id " + userId);
        }
    }

    @Override
    public boolean checkFriendshipStatus (long userId, long friendId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from FRIENDS where FRIEND_ID = ? AND USER_ID = ?",
                userId, friendId);
        if(userRows.next()) {
            return true;
        }
        return false;
    }

    @Override
    public List<User> getFriends(Integer userId) {
        String sqlQuery = "select * from USERS, FRIENDS where USERS.USER_ID = FRIENDS.FRIEND_ID AND FRIENDS.USER_ID = ?";
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

        String sqlQuery = str.toString();
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs, rowNum), userId, friendId);
    }

    private static User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
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

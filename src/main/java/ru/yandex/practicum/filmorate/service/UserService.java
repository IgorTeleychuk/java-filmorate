package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User addNew(User user);

    User update(User user);

    User remove(Integer id);

    User getById(Integer id);

    List<User> getList();

    User addFriend(Integer id, Integer friendId);

    User removeFriend(Integer userId, Integer friendId);

    List<Optional<User>> getCommonFriends(Integer userId, Integer otherId);

    List<User> getAllFriends(Integer userId);

    void nameEqualsLogin(User user);
}

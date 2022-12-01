package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceInterface {
    User addUser(User user);

    User updateUser(User user);

    User removeUser(User user);

    User getUser(Integer id);

    List<User> getListUsers();

    User addFriend(Integer id, Integer friendId);

    User removeFriend(Integer userId, Integer friendId);

    List<User> getCommonFriends(Integer userId, Integer otherId);

    List<User> getAllFriends(Integer userId);
}

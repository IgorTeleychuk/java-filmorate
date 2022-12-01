package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addNewUser(User user);

    User updateUser(User user);

    User removeUser(User user);

    User getUser(Integer id);

    List<User> getListUsers();
}

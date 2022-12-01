package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface{
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public UserService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(User user) {
        return userStorage.addNewUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public User removeUser(User user) {
        return userStorage.removeUser(user);
    }

    @Override
    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }

    @Override
    public List<User> getListUsers() {
        return userStorage.getListUsers();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUser(userId);
        User friendUser = userStorage.getUser(friendId);

        if (user == null) {
            throw new UserNotFoundException("The user was not found");
        }

        if (friendUser == null) {
            throw new UserNotFoundException("The user was not found");
        }

        user.getFriendsId().add(friendId);
        friendUser.getFriendsId().add(userId);

        userStorage.updateUser(friendUser);
        return userStorage.updateUser(user);
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUser(userId);
        User friendUser = userStorage.getUser(friendId);

        if (user == null) {
            throw new UserNotFoundException("The user was now found");
        }

        if (friendUser == null) {
            throw new UserNotFoundException("The user was not found");
        }
        user.getFriendsId().remove(friendUser);
        friendUser.getFriendsId().remove(user);

        userStorage.updateUser(friendUser);
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherId);

        if(user == null) {
            throw new UserNotFoundException("The user was not found");
        }

        if (otherUser == null) {
            throw new UserNotFoundException("The user was not found");
        }

        return user.getFriendsId()
                .stream()
                .filter(otherUser.getFriendsId()::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        return userStorage.getUser(userId).getFriendsId()
                .stream()
                .map(this::getUser)
                .collect(Collectors.toList());

    }
}

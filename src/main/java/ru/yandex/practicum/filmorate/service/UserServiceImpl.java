package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public User addNew(User user) {
        return userStorage.addNew(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public User remove(User user) {
        return userStorage.remove(user);
    }

    @Override
    public Optional<User> getById(Integer id) {
        return Optional.ofNullable(userStorage.getById(id).orElseThrow(() ->
                new UserNotFoundException("The user was not found")));
    }

    @Override
    public List<User> getList() {
        return userStorage.getList();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getById(userId).get();
        User friendUser = userStorage.getById(friendId).get();

        if (user == null) {
            throw new UserNotFoundException("The user was not found");
        }

        if (friendUser == null) {
            throw new UserNotFoundException("The user was not found");
        }

        user.getFriendsId().add(friendId);
        friendUser.getFriendsId().add(userId);

        userStorage.update(friendUser);
        return userStorage.update(user);
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        User user = userStorage.getById(userId).get();
        User friendUser = userStorage.getById(friendId).get();

        if (user == null) {
            throw new UserNotFoundException("The user was now found");
        }

        if (friendUser == null) {
            throw new UserNotFoundException("The user was not found");
        }
        user.getFriendsId().remove(friendUser);
        friendUser.getFriendsId().remove(user);

        userStorage.update(friendUser);
        return userStorage.update(user);
    }

    @Override
    public List<Optional<User>> getCommonFriends(Integer userId, Integer otherId) {
        User user = userStorage.getById(userId).get();
        User otherUser = userStorage.getById(otherId).get();

        if(user == null) {
            throw new UserNotFoundException("The user was not found");
        }

        if (otherUser == null) {
            throw new UserNotFoundException("The user was not found");
        }

        return user.getFriendsId()
                .stream()
                .filter(otherUser.getFriendsId()::contains)
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    @Override
    public List<Optional<User>> getAllFriends(Integer userId) {
        return userStorage.getById(userId).get().getFriendsId()
                .stream()
                .map(this::getById)
                .collect(Collectors.toList());

    }

    @Override
    public Map<Integer, User> findUser() {
        return userStorage.findUser();
    }
}

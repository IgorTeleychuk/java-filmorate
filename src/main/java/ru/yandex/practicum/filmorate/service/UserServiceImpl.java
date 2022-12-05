package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
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
        nameEqualsLogin(user);
        if (!user.getLogin().contains(" ")) {
            return userStorage.addNew(user);
        } else {
            throw new ValidationException("The login must not contain gaps");
        }
    }

    @Override
    public User update(User user) {
        if (userStorage.getById(user.getId()).isPresent()){
            nameEqualsLogin(user);
        return userStorage.update(user);
        } else {
            throw new UserNotFoundException("The user was not found");
        }
    }

    @Override
    public User remove(Integer id) {
        return userStorage.remove(id);
    }

    @Override
    public User getById(Integer id) {
        return userStorage.getById(id).orElseThrow(() ->
                new UserNotFoundException("The user was not found"));
    }

    @Override
    public List<User> getList() {
        return userStorage.getList();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        if (userStorage.getById(userId).isPresent() && userStorage.getById(friendId).isPresent()) {
            User user = userStorage.getById(userId).get();
            User friendUser = userStorage.getById(friendId).get();
            user.getFriendsId().add(friendId);
            friendUser.getFriendsId().add(userId);
            userStorage.update(friendUser);

            return userStorage.update(user);
        } else {
            throw new UserNotFoundException("User or Film was not found");
        }

    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        if (userStorage.getById(userId).isPresent() && userStorage.getById(friendId).isPresent()) {
            User user = userStorage.getById(userId).get();
            User friendUser = userStorage.getById(friendId).get();
            user.getFriendsId().remove(friendUser);
            friendUser.getFriendsId().remove(user);
            userStorage.update(friendUser);

            return userStorage.update(user);
        } else {
            throw new UserNotFoundException("User or Friend was not found");
        }

    }

    @Override
    public List<Optional<User>> getCommonFriends(Integer userId, Integer otherId) {
        if (userStorage.getById(userId).isPresent() && userStorage.getById(otherId).isPresent()) {
            User user = userStorage.getById(userId).get();
            User otherUser = userStorage.getById(otherId).get();

            return user.getFriendsId()
                    .stream()
                    .filter(otherUser.getFriendsId()::contains)
                    .map(userStorage::getById)
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("User or Friend was not found");
        }
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        if(userStorage.getById(userId).isPresent()) {
            return userStorage.getById(userId).get().getFriendsId()
                    .stream()
                    .map(this::getById)
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("The user was not found");
        }
    }

    @Override
    public void nameEqualsLogin(User user) {
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
    }
}

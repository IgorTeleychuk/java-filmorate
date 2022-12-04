package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    protected static int nextUserId = 0;

    @Override
    public User addNew(User user) {
            nextUserId++;
            user.setId(nextUserId);
            users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
            user.setId(user.getId());
            users.put(user.getId(), user);
        return user;
    }

    @Override
    public User remove(User user) {
            User removeUser = users.get(user.getId());
            users.remove(user.getId());
            return removeUser;
    }

    @Override
    public Optional<User> getById(Integer id) {
        return Optional.of(users.get(id));
    }

    @Override
    public List<User> getList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Map<Integer, User> findUser() {
        return users;
    }
}

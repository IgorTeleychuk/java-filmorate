package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    protected static int nextUserId = 0;

    @Override
    public User addNewUser(User user) {
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        if (!user.getLogin().contains(" ")) {
            nextUserId++;
            user.setId(nextUserId);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("The login must not contain gaps");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if(users.containsKey(user.getId())){
            user.setId(user.getId());
            users.put(user.getId(), user);
        } else {
            throw new UserNotFoundException("The user was not found");
        }
        return user;
    }

    @Override
    public User removeUser(User user) {
        if(!users.containsKey(user.getId())) {
            throw new UserNotFoundException("The user was not found");
        } else {
            User removeUser = users.get(user.getId());
            users.remove(user.getId());
            return removeUser;
        }
    }

    @Override
    public User getUser(Integer id) {
        if(users.containsKey(id)){
            return users.get(id);
        } else {
            throw new UserNotFoundException("The user was not found");
        }
    }

    @Override
    public List<User> getListUsers() {
        return new ArrayList<>(users.values());
    }
}

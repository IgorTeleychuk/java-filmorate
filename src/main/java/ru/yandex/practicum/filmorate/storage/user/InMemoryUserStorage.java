package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
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
    public User remove(Integer id) {
            User removeUser = users.get(id);
            users.remove(id);
            return removeUser;
    }

    @Override
    public Optional<User> getById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getList() {
        return new ArrayList<>(users.values());
    }
}

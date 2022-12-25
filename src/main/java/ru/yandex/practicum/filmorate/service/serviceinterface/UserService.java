package ru.yandex.practicum.filmorate.service.serviceinterface;

import ru.yandex.practicum.filmorate.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User addNew(User user);

    User update(User user);

    void remove(User user);

    User getById(Integer id);

    List<User> getList();

    void addFriend(Integer id, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<User> getCommonFriends(Integer userId, Integer otherId);

    List<User> getFriends(Integer userId);

    void nameEqualsLogin(User user);
}

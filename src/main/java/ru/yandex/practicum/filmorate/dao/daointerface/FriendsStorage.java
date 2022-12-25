package ru.yandex.practicum.filmorate.dao.daointerface;

public interface FriendsStorage {
    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    boolean checkFriendshipStatus (long userId, long friendId);
}

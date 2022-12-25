package ru.yandex.practicum.filmorate.dao.daointerface;

public interface LikesStorage {
    void addLike(int filmId, long userId);

    void deleteLike(int filmId, long userId);
}

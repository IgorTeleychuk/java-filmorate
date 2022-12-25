package ru.yandex.practicum.filmorate.dao.daointerface;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<Mpa> findAll();

    Optional<Mpa> getMPAByID(int id);
}

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.daointerface.MpaStorage;
import ru.yandex.practicum.filmorate.service.serviceinterface.MpaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaStorage;

    @Override
    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    @Override
    public Mpa getMpaByID(int id) {
        return mpaStorage.getMpaByID(id).orElseThrow(() -> new MpaNotFoundException("Rating MPA with " + id +
                " not found."));
    }
}

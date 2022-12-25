package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.daointerface.MpaStorage;
import ru.yandex.practicum.filmorate.service.serviceinterface.MpaService;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {

    MpaStorage mpaStorage;

    @Autowired
    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    @Override
    public Mpa getMPAByID(int id) {
        return mpaStorage.getMPAByID(id).orElseThrow(() -> new MpaNotFoundException("Rating MPA with " + id +
                " not found."));
    }
}

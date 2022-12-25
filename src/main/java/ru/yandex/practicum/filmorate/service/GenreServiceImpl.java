package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.daointerface.GenreStorage;
import ru.yandex.practicum.filmorate.service.serviceinterface.GenreService;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    GenreStorage genreStorage;

    @Autowired
    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    @Override
    public Genre getGenreByID(int id) {
        return genreStorage.getGenreByID(id).orElseThrow(() -> new GenreNotFoundException("Genre with " + id + " not found."));
    }
}

package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void validationTest() {
        Film film1 = new Film();
        film1.setName("Test");
        film1.setDescription("There are too many characters in this test, given the undeniable fact that the " +
                "currently available number is only slightly less than the maximum possible limit and there " +
                "simply cannot be any inch 100%."); // test description
        film1.setReleaseDate(LocalDate.of(1991,10,10));
        film1.setDuration(60);

        Film film6 = new Film(); // test null

        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film1));
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film6));

    }
}
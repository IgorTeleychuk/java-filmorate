package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void createUser() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("Login");
        user.setName("Test");
        user.setBirthday(LocalDate.of(2000, 12, 10));

        User userLoginEmpty = new User();
        userLoginEmpty.setEmail("test@mail.ru");
        userLoginEmpty.setName("Test");
        userLoginEmpty.setBirthday(LocalDate.of(2000, 12, 10));

        User userIncorrectLogin = new User();
        userIncorrectLogin.setEmail("test@mail.ru");
        userIncorrectLogin.setLogin("Login test");
        userIncorrectLogin.setName("Test");
        userIncorrectLogin.setBirthday(LocalDate.of(2000, 12, 10));

        User userBirthdayAfterDateNow = new User();
        userBirthdayAfterDateNow.setEmail("test@mail.ru");
        userBirthdayAfterDateNow.setLogin("Login");
        userBirthdayAfterDateNow.setName("Test");
        userBirthdayAfterDateNow.setBirthday(LocalDate.of(2023, 12, 10));

        assertThrows(ValidationException.class, () -> userController.addNewUser(userLoginEmpty));
        assertThrows(ValidationException.class, () -> userController.addNewUser(userIncorrectLogin));
        assertThrows(ValidationException.class, () -> userController.addNewUser(userBirthdayAfterDateNow));

    }
}
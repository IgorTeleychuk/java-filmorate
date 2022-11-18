package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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

        User userEmailEmpty = new User();
        userEmailEmpty.setEmail(null);
        userEmailEmpty.setLogin("Login");
        userEmailEmpty.setName("Test");
        userEmailEmpty.setBirthday(LocalDate.of(2000, 12, 10));

        User userIncorrectEmail = new User();
        userIncorrectEmail.setEmail("testmailru");
        userIncorrectEmail.setLogin("Login");
        userIncorrectEmail.setName("Test");
        userIncorrectEmail.setBirthday(LocalDate.of(2000, 12, 10));

        User userLoginEmpty = new User();
        userLoginEmpty.setEmail("test@mail.ru");
        userLoginEmpty.setName("Test");
        userLoginEmpty.setBirthday(LocalDate.of(2000, 12, 10));

        User userIncorrectLogin = new User();
        userIncorrectLogin.setEmail("test@mail.ru");
        userIncorrectLogin.setLogin("Login test");
        userIncorrectLogin.setName("Test");
        userIncorrectLogin.setBirthday(LocalDate.of(2000, 12, 10));

        User userNameEmpty = new User();
        userNameEmpty.setEmail("test@mail.ru");
        userNameEmpty.setLogin("Login");
        userNameEmpty.setBirthday(LocalDate.of(2000, 12, 10));

        User userBirthdayAfterDateNow = new User();
        userBirthdayAfterDateNow.setEmail("test@mail.ru");
        userBirthdayAfterDateNow.setLogin("Login");
        userBirthdayAfterDateNow.setName("Test");
        userBirthdayAfterDateNow.setBirthday(LocalDate.of(2023, 12, 10));

        assertThrows(ValidationException.class, () -> userController.addNewUser(userEmailEmpty));
        assertThrows(ValidationException.class, () -> userController.addNewUser(userIncorrectEmail));
        assertThrows(ValidationException.class, () -> userController.addNewUser(userLoginEmpty));
        assertThrows(ValidationException.class, () -> userController.addNewUser(userIncorrectLogin));
        assertThrows(ValidationException.class, () -> userController.addNewUser(userNameEmpty));
        assertThrows(ValidationException.class, () -> userController.addNewUser(userBirthdayAfterDateNow));

    }
}
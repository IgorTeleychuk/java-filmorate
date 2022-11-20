package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    protected static int nextUserId = 0;

    @PostMapping(value = "/users")
    public User addNewUser(@Valid @RequestBody User user) throws ValidationException {
        if (userValidation(user)) {
//            if(user.getName() == null){
//                user.setName(user.getLogin());
//            }
            user.setId(getNextUserId());
            users.put(user.getId(), user);
            log.info("A new user has been added");
            return user;
        } else {
            log.info("The user failed validation");
            throw new ValidationException("User was not added");
        }
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (userValidation(user)) {
            if (!(users.keySet().contains(user.getId()))) {
                throw new ValidationException("User unknown");
            }
            users.put(user.getId(), user);
            log.info("User was updated");
            return user;
        } else {
            log.info("The user failed validation");
            throw new ValidationException("User was not updated");
        }
    }

    @GetMapping("/users")
    public Collection<User> returnUsers() {
        Collection<User> userList = users.values();
        log.info("Received a request for a list of users");
        return userList;
    }

    private boolean userValidation(User user) throws ValidationException {
        boolean b = true;
        if ((user.getEmail() == null) || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            b = false;
            throw new ValidationException("The email cannot be empty and must contain the character @");
        }
        if ((user.getLogin() == null) ||  user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            b = false;
            throw new ValidationException("The login cannot be empty and contain spaces");
        }
        if (user.getName() == null  || user.getName().isBlank() || user.getName().isEmpty()) {
            b = true;
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            b = false;
            throw new ValidationException("The date of birth cannot be in the future");
        }
        return b;
    }

    public static int getNextUserId() {
        return ++nextUserId;
    }
}

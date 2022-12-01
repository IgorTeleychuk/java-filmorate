package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceInterface;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceInterface userService;

    @GetMapping
    public List<User> returnAllUsers() {
        log.info("Request GET /users received");
        return userService.getListUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        log.info("Request GET /users/{id} received");
        User user = userService.getUser(id);
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        log.info("Request GET /users/{id}/friends received");
        return  userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Request GET /users/{id}/friends/common/{otherId}");
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addNewUser(@Valid @RequestBody User user) {
        log.info("Request GET /users");
        User addUser = userService.addUser(user);
        log.info("A new user has been added");
        return addUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Request GET /users");
        User updateUser = userService.updateUser(user);
        log.info("User was updated");
        return updateUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Request GET /users/{id}/friends/{friendId}");
        User user = userService.addFriend(id, friendId);
        log.info("Friend was added");
        return user;
    }

   @DeleteMapping
    public User removeUser(@Valid @RequestBody User user) {
       log.info("Request GET /users/");
       User removeUser = userService.removeUser(user);
       log.info("User was deleted");
       return removeUser;
   }

   @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
       log.info("Request GET /users/{id}/friends/{friendId}");
       User friend = userService.removeFriend(id, friendId);
       log.info("Friend was deleted");
       return friend;
   }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> returnAll() {
        log.info("Request GET /users received");
        return userService.getList();
    }

    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable Integer id) {
        log.info("Request GET /users/{id} received");
        if(id > userService.getList().size()){
            throw new UserNotFoundException("The user was not found");

        } else {
            return userService.getById(id);
        }
    }

    @GetMapping("/{id}/friends")
    public List<Optional<User>> getAllFriends(@PathVariable Integer id) {
        log.info("Request GET /users/{id}/friends received");
        return  userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<Optional<User>> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Request GET /users/{id}/friends/common/{otherId}");
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addNew(@Valid @RequestBody User user) {
        log.info("Request POST /users");
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        User addUser;
        if (!user.getLogin().contains(" ")) {
        addUser = userService.addNew(user);
        } else {
            throw new ValidationException("The login must not contain gaps");
        }
        log.info("A new user has been added");
        return addUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Request PUT /users");
        if(userService.findUser().containsKey(user.getId())){
        User updateUser = userService.update(user);
        log.info("User was updated");
        return updateUser;
        } else {
            throw new UserNotFoundException("The user was not found");
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Request PUT /users/{id}/friends/{friendId}");
        if (id < 1 || friendId < 1) {
            throw new UserNotFoundException("Incorrect Id");
        } else {
            log.info("Friend was added");
            User user = userService.addFriend(id, friendId);
            return userService.addFriend(id, friendId);
        }
    }

   @DeleteMapping("/{id}")
    public User deleteById(@PathVariable Integer id) {
       log.info("Request DELETE /users/{id}");
       if(!userService.findUser().containsKey(id)) {
           throw new UserNotFoundException("The user was not found");
       } else {
           User removeUser = userService.remove(userService.getById(id).get());
           log.info("User was deleted");
           return removeUser;
       }
   }

   @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
       log.info("Request DELETE /users/{id}/friends/{friendId}");
       if (id < 1 || friendId < 1) {
           throw new UserNotFoundException("Incorrect Id");
       } else {
           log.info("Friend was deleted");
           User user = userService.removeFriend(id, friendId);
           return user;
       }
   }
}

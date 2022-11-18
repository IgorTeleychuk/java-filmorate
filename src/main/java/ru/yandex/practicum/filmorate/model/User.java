package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @Email
    private String email;
    private String login;
    private String name;
    @NotNull
    private LocalDate birthday;
}

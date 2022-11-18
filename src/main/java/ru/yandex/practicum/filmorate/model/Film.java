package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    private int duration;
}

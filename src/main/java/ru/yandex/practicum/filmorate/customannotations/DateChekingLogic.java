package ru.yandex.practicum.filmorate.customannotations;

import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateChekingLogic implements ConstraintValidator<DateCheking, LocalDate> {
    private String annotationLLocalDate;
    @Override
    public void initialize(DateCheking constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.annotationLLocalDate = constraintAnnotation.date();
    }

    @SneakyThrows
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate date = LocalDate.parse(annotationLLocalDate,  DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        if (localDate.isAfter(date)) {
            return true;
        } else {
            return false;
        }
    }
}

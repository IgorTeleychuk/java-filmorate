package ru.yandex.practicum.filmorate.customannotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;;

@Documented
@Constraint(validatedBy = DateChekingLogic.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateCheking {
    String date();
    String message() default "Wrong Date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

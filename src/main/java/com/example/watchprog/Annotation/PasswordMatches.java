package com.example.watchprog.Annotation;


import com.example.watchprog.Validations.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Invalid Email";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payLoad() default {};
}

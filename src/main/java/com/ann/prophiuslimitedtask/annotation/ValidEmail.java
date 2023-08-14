package com.ann.prophiuslimitedtask.annotation;

import com.ann.prophiuslimitedtask.validator.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {
    String message() default "Invalid email address";  // Default error message if validation fails

    Class<?>[] groups() default {};  // Validation groups, useful for more advanced validation scenarios

    Class<? extends Payload>[] payload() default {};  // Additional data to attach to the validation result
}

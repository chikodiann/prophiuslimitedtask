package com.ann.prophiuslimitedtask.annotation;

import com.ann.prophiuslimitedtask.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Invalid password";  // Default error message if validation fails

    Class<?>[] groups() default {};  // Validation groups, useful for more advanced validation scenarios

    Class<? extends Payload>[] payload() default {};  // Additional data to attach to the validation result
}

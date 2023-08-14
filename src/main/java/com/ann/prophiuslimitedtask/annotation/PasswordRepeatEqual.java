package com.ann.prophiuslimitedtask.annotation;

import com.ann.prophiuslimitedtask.validator.PasswordRepeatEqualValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordRepeatEqualValidator.class)
public @interface PasswordRepeatEqual {
    String message() default "Password mismatch";  // Default error message if validation fails

    String passwordFieldFirst();  // Specifies the first password field name

    String passwordFieldSecond();  // Specifies the second password field name

    Class<?>[] groups() default {};  // Validation groups, useful for more advanced validation scenarios

    Class<? extends Payload>[] payload() default {};  // Additional data to attach to the validation result
}

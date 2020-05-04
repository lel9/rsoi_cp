package ru.bmstu.cp.rsoi.drug.model.annotation;

import ru.bmstu.cp.rsoi.drug.model.validator.NullOrNotBlankValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target( {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    String message() default "{javax.validation.constraints.Pattern.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}

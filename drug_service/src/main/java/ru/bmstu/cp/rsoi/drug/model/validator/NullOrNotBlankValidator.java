package ru.bmstu.cp.rsoi.drug.model.validator;

import ru.bmstu.cp.rsoi.drug.model.annotation.NullOrNotBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    public void initialize(NullOrNotBlank parameters) {
        // Nothing to do here
    }

    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return str == null || str.trim().length() > 0;
    }
}
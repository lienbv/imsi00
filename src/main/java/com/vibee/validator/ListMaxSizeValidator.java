package com.vibee.validator;

import com.vibee.annotation.ListMaxSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ListMaxSizeValidator implements ConstraintValidator<ListMaxSize, Object> {
    int maxSize;

    public void initialize(final ListMaxSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            List<?> list = (List<?>) value;
            if (list.size() > maxSize) {
                return false;
            }
            return true;
        } catch (final Exception ex) {
            return false;
        }

    }
}

package com.vibee.annotation;

import com.vibee.validator.FieldExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = FieldExistsValidator.class)
@Documented
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldExists {
    String message() default "Fields are not exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String dataFieldName();

    String listFieldName();

    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        FieldExists[] value();
    }
}
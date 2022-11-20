package com.vibee.annotation;

import com.vibee.enums.CatalogEnum;
import com.vibee.validator.ValidCatalogValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = ValidCatalogValidator.class)
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCatalog {
    String message() default "Value is not existed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    CatalogEnum catalog();

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ValidCatalog[] value();
    }
}
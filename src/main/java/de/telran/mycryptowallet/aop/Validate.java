package de.telran.mycryptowallet.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 * The class is in development.
 * It is not used in the project.
 * @author Alexander Isai on 03.02.2024.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    String message() default "ERROR VALIDATE";
}

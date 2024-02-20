package com.jonahseguin.drink.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Require {

    /**
     * Returns the permission node required to perform a command.
     *
     * @return the value of the annotation
     */
    String value();

    /**
     * Returns the default message when the user does not have permission to perform a command.
     * {command} will be replaced with the command label.
     *
     * @return the default permission message
     */
    String message() default "";

}

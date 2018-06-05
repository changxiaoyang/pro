package com.cxy890.elasticsearch.annotations;

import com.cxy890.elasticsearch.enums.EField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author BD-PC27
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsFiled {

    String value() default "";

    EField type() default EField.TEXT;

    String analyzer() default "";

    String format() default "";

    boolean ignore() default false;

}

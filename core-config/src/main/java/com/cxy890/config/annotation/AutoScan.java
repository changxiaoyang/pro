package com.cxy890.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动扫描注解
 *
 * Created by cxy890 on 2017/8/12.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoScan {

    Level level() default Level.SERVICE;

}

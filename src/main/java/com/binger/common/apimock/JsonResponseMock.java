package com.binger.common.apimock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 相对与classpath, resoruce目录下路径, e.g. mock/foundation/xxx.mock
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonResponseMock {
    String value() default "";
}

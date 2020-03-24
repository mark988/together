package com.together.demo.annotation;

import java.lang.annotation.*;

/**
 * 这个注解可以添加跟踪日志
 * @author maxiaoguang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface EagleEye {
    String desc() default "";
}

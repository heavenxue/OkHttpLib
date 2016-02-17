package com.lixue.admin.okhttputils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在使用response的时候，如果在要转换的目标类上加了此注解，那么将会在返回的json串中取出键为此注解的值的json串
 * 来转换为目标类
 * Created by Administrator on 2015/12/11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AntnResponseBody {
    public String value() default "";
    public int resId() default 0;
}

package cn.kj120.study.net;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface PCompoment {
    String value() default "";
}

package com.ecm.annotation;

import java.lang.annotation.*;

/**
 * Created by rongyaowen
 * on 2019/2/27.
 * 不存储字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Transparent {
}

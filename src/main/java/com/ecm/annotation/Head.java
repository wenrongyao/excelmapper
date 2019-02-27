package com.ecm.annotation;

import java.lang.annotation.*;

/**
 * Created by rongyaowen
 * on 2019/2/27.
 * 指定字段的标题名称
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Head {
    /**
     * 子段名称
     *
     * @return
     */
    String value();

    /**
     * 排序子段
     *
     * @return
     */
    int orderBy();

}

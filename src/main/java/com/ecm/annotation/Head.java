package com.ecm.annotation;

/**
 * Created by rongyaowen
 * on 2019/2/27.
 * 指定字段的标题名称
 */
public @interface Head {
    /**
     * 子段名称
     *
     * @return
     */
    String value() default "";

    /**
     * 排序子段
     *
     * @return
     */
    int orderBy() default 0;
}

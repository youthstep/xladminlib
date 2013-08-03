package com.xunlei.common.dao.annotation;

import java.lang.annotation.*;

/**
 * User: IceRao
 * Date: 2010-3-2
 * Time: 10:07:19
 * Description:此注解用于标识一个vo对应的数据库表名
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name();
}

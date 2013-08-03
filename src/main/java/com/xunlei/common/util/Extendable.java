/*
 * Extendable.java
 *
 * Created on 2007年8月23日, 下午9:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.util;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.*;

/**
 *
 * @author 张金雄
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Extendable {
}

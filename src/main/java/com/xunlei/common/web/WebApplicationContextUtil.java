/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xunlei.common.web;

import org.springframework.context.ApplicationContext;

/**
 *
 * @author IceRao
 */
public class WebApplicationContextUtil {
    //在web进行初始化时，将Spring的上下文设置在此
    public static ApplicationContext webApplicationContext=null;
}

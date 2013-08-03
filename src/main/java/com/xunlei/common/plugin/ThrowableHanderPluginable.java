/*
 * ThrowableHanderPluginable.java
 *
 * Created on 2007年9月27日, 下午9:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理插件
 * @author 张金雄 
 */
public interface ThrowableHanderPluginable extends Pluginable {
    
    /**
     * 返回false表示不再往下执行异常处理了
     *
     */
    public boolean sendProcessingException(String webroot, Throwable t, HttpServletRequest request, HttpServletResponse response);
    
}

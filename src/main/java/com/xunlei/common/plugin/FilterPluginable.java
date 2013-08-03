/*
 * FilterPluginable.java
 *
 * Created on 2007年9月19日, 下午4:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.plugin;

import com.xunlei.common.event.XLRuntimeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 过滤器插件
 * @author 张金雄 
 */
public interface FilterPluginable extends Pluginable {
    
    public enum ResultType {
        ABORT,
        BREAK,
        GOON;
    }
    
    /**
     * 过滤插件
     * @param webroot web路径
     * @param request 请求
     * @param response 响应
     * @return 一个枚举类型的结果参数：ABORT,BREAK,GOON;
     */
    public ResultType doBeforeProcessing(String webroot, HttpServletRequest request, HttpServletResponse response) throws XLRuntimeException;
}

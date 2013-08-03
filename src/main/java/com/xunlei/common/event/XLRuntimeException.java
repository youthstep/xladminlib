/*
 * XLRuntimeException.java
 *
 * Created on 2006年11月3日, 下午5:43
 *
 *edit on 2011-11-29 增加异常抛出时的日志记录
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.event;

import org.apache.log4j.Logger;

/**
 *
 * @author kamhung
 */
@SuppressWarnings("serial")
public class XLRuntimeException extends RuntimeException {
	private Logger logger = Logger.getLogger(XLRuntimeException.class);
    
    public XLRuntimeException() {
        super();
        logger.error("未知错误");
    }
    
    public XLRuntimeException(String message) {
        super(message);
        logger.error(message);
    }
    
    public XLRuntimeException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message);
    }
    
    public XLRuntimeException(Throwable cause) {
        super(cause);
        logger.error(cause.toString());
    }
}

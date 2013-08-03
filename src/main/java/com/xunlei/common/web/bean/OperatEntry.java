/*
 * OperatEntry.java
 *
 * Created on 2006年11月10日, 下午3:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.web.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  张金雄
 */
public class OperatEntry implements java.io.Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String operateno;
    private final String operatename;
    private final int index;
    
    private OperatEntry(String operateno, String operatename, int index){
        this.operateno =operateno;
        this.operatename =operatename;
        this.index =index;
    }
    
    public String getOperateno() {
        return operateno;
    }
    
    public String getOperatename() {
        return operatename;
    }
    
    public int getIndex() {
        return index;
    }
    
    @Override
    public String toString() {
        return "OperatEntry[操作编号:" + operateno +",操作名称:"+ operatename + ", 位置:" + index + "]";
    }
}

/*
 * LibClassDPluginable.java
 *
 * Created on 2007年9月20日, 下午4:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.plugin;

import com.xunlei.libfun.vo.LibClassD;

/**
 *  更新系统配置的插件
 * @author 张金雄
 */
public interface LibClassDPluginable extends Pluginable {
    
    public enum Action {
        INSERT,
        UPDATE,
        REMOVE;
    }
    
    public void update(LibClassD data, Action action);
}

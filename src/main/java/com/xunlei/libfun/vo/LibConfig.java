package com.xunlei.libfun.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.util.Extendable;
import com.xunlei.common.web.WebApplicationContextUtil;
import com.xunlei.libfun.bo.ILibConfigBo;


/**
 * 系统配置的VO类，提供初始化系统常量的方法
 * @author 童辉
 */

public class LibConfig  implements java.io.Serializable {
    @Extendable
	private static Logger logger = Logger.getLogger(LibConfig.class);
	
    /**
     * ConfigNo : LibConfig 对象 映射关系
     */
    @Extendable
    private static Map<String, LibConfig> libconfigMap = null;
    
    // Fields
    private long seqid = 0L;
    private String configno;
    private String configname;
    private String configvalue;
    private String remark;
    private String editby;
    private String edittime;

    /**
     * 载入 libconfigMap，将系统常量读入缓存
     */
    public static void loadLibconfig() {
    	libconfigMap = new HashMap<String, LibConfig>();
	
		List<LibConfig> libs;
		ILibConfigBo libConfigBo = WebApplicationContextUtil.webApplicationContext.getBean(ILibConfigBo.class);
		libs = libConfigBo.getAllLibConfig();
		for (LibConfig lib : libs) {
			libconfigMap.put(lib.getConfigno(), lib);
		}    	
		logger.debug("载入 libconfigMap 完成");
		
    }
    
    /**
     * 置空 libconfigMap，当进行新增、修改或者删除系统常量时要执行此方法，清空缓存
     */
    public static void clearLibconfig() {
    	libconfigMap = null;
		logger.debug("置空 libconfigMap 完成");
    }
    
    /**
     * 从缓存中取 configno 对应的 LibConfig 对象
     * @param configno  配置编号
     * @return LibConfig 对象
     */
    public static LibConfig getLibConfig(String configno) {
    	if(libconfigMap == null) loadLibconfig();
    	return libconfigMap.get(configno);
    }
    
    /**
     * 从缓存中取某 LibConfig 的参数数组
     * @return ArrayList<LibConfig>
     */
    public static List<LibConfig> getLibConfigList() {
    	if(libconfigMap == null) loadLibconfig();
    	List<LibConfig> list = new ArrayList<LibConfig>();
    	Iterator<Entry<String, LibConfig>> it = libconfigMap.entrySet().iterator();
    	while(it.hasNext()) {
    		Map.Entry<String, LibConfig> entry = (Map.Entry<String, LibConfig>)it.next();
    		list.add(entry.getValue());
    	}
    	return list;
    }
    
    /**
     * 向系统配置缓存数组中增加一条配置信息
     * @author liheng
     * 2011-9-20
     * @param libConfig
     */
    public static void insertLibConfig(LibConfig libConfig){
    	String configno = libConfig.getConfigno();
    	if(libconfigMap == null) 
    		throw new XLRuntimeException("无法找到要增加的缓存所在系统配置：LibConfig(configno=" + configno +"）");
    	else if(libconfigMap.containsKey(configno))
    		throw new XLRuntimeException("要增加的缓存配置：LibConfig(configno)=(" + configno +")已存在，更新失败");
    	libconfigMap.put(configno, libConfig);
    	logger.debug("增加缓存 configno=" + configno + " 完成");
    }
    
    /**
     * 更新系统配置缓存数组中的一条配置信息
     * @author liheng
     * 2011-9-20
     * @param libConfig
     */
    public static void updateLibConfig(LibConfig libConfig){
    	String configno = libConfig.getConfigno();
    	if(libconfigMap == null || !libconfigMap.containsKey(configno))
    		throw new XLRuntimeException("无法找到要更新的缓存所在系统配置：LibConfig(configno=" + configno + ")，更新失败 ");
    	libconfigMap.put(configno, libConfig);
    	logger.debug("更新缓存 configno=" + configno + " 完成");
    	
    }
    
    /**
     * 删除系统配置缓存数组中得一条配置信息
     * @author liheng
     * 2011-9-20
     * @param libConfig
     */
    public static void deleteLibConfig(LibConfig libConfig){
    	String configno = libConfig.getConfigno();
    	if(libconfigMap == null || !libconfigMap.containsKey(configno))
    		throw new XLRuntimeException("无法找到要删除的缓存所在系统配置：LibConfig(configno=" + configno + ")，删除失败 ");
    	libconfigMap.remove(configno);
    	logger.debug("删除缓存 configno=" + configno + " 完成");
    }
    
    /**
     * 根据配置编号得到该系统常量的配置值
     * @param configno 配置编号
     * @return String型的系统常量配置值
     */
    public static String getValue(String configno) {
        try{
        return getLibConfig(configno).getConfigvalue();
        }
        catch(Exception ex){
            return null;
        }
    }
    
    /**
     * 根据配置编号得到该系统常量的配置值，如果配置值为null则用默认值代替
     * @param configno 配置编号
     * @param defaultValue 默认的系统常量配置值
     * @return String型的系统常量配置值
     */
    public static String getValue(String configno, String defaultValue) {
        String v = getValue(configno);
        return (v == null) ? defaultValue : v;
    }
    
    /**
     * 根据配置编号得到该系统常量的配置值
     * @param configno 配置编号
     * @return int型的系统常量配置值
     */
    public static int getIntValue(String configno) {
        return  Integer.parseInt(getValue(configno));
    }
    
    /**
     * 根据配置编号得到该系统常量的配置值，如果配置值为null则用默认值代替
     * @param configno 配置编号
     * @param defaultValue 默认的系统常量配置值
     * @return int型的系统常量配置值
     */
    public static int getIntValue(String configno, int defaultValue) {
        String v = getValue(configno);
        return (v == null) ? defaultValue : Integer.parseInt(v);
    }
    
    /**
     * 根据配置编号得到该系统常量的配置值
     * @param configno 配置编号
     * @return float型的系统常量配置值
     */
    public static float getFloatValue(String configno) {
        return  Float.parseFloat(getValue(configno));
    }
    
    /**
     * 根据配置编号得到该系统常量的配置值，如果配置值为null则用默认值代替
     * @param configno 配置编号
     * @param defaultValue 默认的系统常量配置值
     * @return float型的系统常量配置值
     */
    public static float getFloatValue(String configno, float defaultValue) {
        String v = getValue(configno);
        return (v == null) ? defaultValue : Float.parseFloat(v);
    }
    
    /** default constructor */
    public LibConfig() {
    }
    
    /** minimal constructor */
    public LibConfig(String configno, String configname, String configvalue, String editby, String edittime) {
        this.configno = configno;
        this.configname = configname;
        this.configvalue = configvalue;
        this.editby = editby;
        this.edittime = edittime;
    }
    
    /** full constructor */
    public LibConfig(String configno, String configname, String configvalue, String remark, String editby, String edittime) {
        this.configno = configno;
        this.configname = configname;
        this.configvalue = configvalue;
        this.remark = remark;
        this.editby = editby;
        this.edittime = edittime;
    }
    
    
    
    // Property accessors
    
    public long getSeqid() {
        return this.seqid;
    }
    
    public void setSeqid(long seqid) {
        this.seqid = seqid;
    }
    
    public String getConfigno() {
        return this.configno;
    }
    
    public void setConfigno(String configno) {
        this.configno = configno;
    }
    
    public String getConfigname() {
        return this.configname;
    }
    
    public void setConfigname(String configname) {
        this.configname = configname;
    }
    
    public String getConfigvalue() {
        return this.configvalue;
    }
    
    public void setConfigvalue(String configvalue) {
        this.configvalue = configvalue;
    }
    
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getEditby() {
        return this.editby;
    }
    
    public void setEditby(String editby) {
        this.editby = editby;
    }
    
    public String getEdittime() {
        return this.edittime;
    }
    
    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }
}
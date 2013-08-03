package com.xunlei.libfun.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.directwebremoting.annotations.DataTransferObject;

import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.util.Extendable;
import com.xunlei.common.web.WebApplicationContextUtil;
import com.xunlei.libfun.bo.ILibClassDBo;



/**
 * 系统数组的VO类，提供初始化系统常量数组的方法
 * @author 童辉
 */
@DataTransferObject
public class LibClassM  implements java.io.Serializable {
	
	@Extendable
	private static Logger logger = Logger.getLogger(LibClassM.class);
	
	@Extendable
	private static Map<String, Map<String, LibClassD>> LibClassMMap = new HashMap<String, Map<String, LibClassD>>();
	    
    private long seqid = 0L;
    private String classno;
    private String classname;
    private String remark ="";
    private String editby;
    private String edittime;   
  
    /**
     * 载入 classno 的参数，将系统常量数组读入缓存
     * @param classno 数组编号
     */
    public static void loadLibClassM(String classno) {
    	if(LibClassMMap == null) LibClassMMap = new HashMap<String, Map<String, LibClassD>>();
    	
    	LibClassD lcd = new LibClassD();
    	lcd.setClassno(classno);
    	ILibClassDBo libClassDBo = WebApplicationContextUtil.webApplicationContext.getBean(ILibClassDBo.class);
    	List<LibClassD> list = (List<LibClassD>)libClassDBo.queryLibClassds(lcd, null).getDatas();
    	if(list == null || list.size() == 0) 
    		throw new XLRuntimeException("无匹配的LibClassD数据, classno=" + classno);
		LinkedHashMap<String, LibClassD> libClassDMap = new LinkedHashMap<String, LibClassD>();
    	for(LibClassD one : list) {
    		libClassDMap.put(one.getItemno(), one);
    	}
    	LibClassMMap.put(classno, libClassDMap);
    	logger.debug("载入 classno=" + classno + " 的参数完成");
    }
    
    /**
     * 置空 classno 的参数，当进行新增、修改或者删除系统常量数组时要执行此方法，清空缓存
     * @param classno 数组编号
     */
    public static void clearLibClassM(String classno) {
    	if(LibClassMMap == null) return;
        if(classno==null) LibClassMMap.clear();
    	if(LibClassMMap.containsKey(classno)) LibClassMMap.remove(classno);
    	logger.debug("置空 classno=" + classno + " 的参数完成");
    }
    
    /**
     * 从缓存中取某 classno 的参数数组
     * @param classno 数组编号
     * @return LinkedHashMap<itemno, LibClassD>
     */
    public static Map<String, LibClassD> getLibClassDMap(String classno) {
    	if(LibClassMMap == null || !LibClassMMap.containsKey(classno)) loadLibClassM(classno);
        return LibClassMMap.get(classno);
    }
    
    /**
     * 从缓存中取某 classno 的参数数组
     * @param classno 数组编号
     * @return ArrayList<LibClassD>
     */
    public static List<LibClassD> getLibClassDList(String classno) {
    	Map<String, LibClassD> map = getLibClassDMap(classno);
    	List<LibClassD> list = new ArrayList<LibClassD>();
    	Iterator<Entry<String, LibClassD>> it = map.entrySet().iterator();
    	while(it.hasNext()) {
    		Map.Entry<String, LibClassD> entry = (Map.Entry<String, LibClassD>)it.next();
    		list.add(entry.getValue());
    	}
    	return list;
    }
    
    /**
     * 从缓存中取某 classno 的参数数组
     * @param classno  数组编号
     * @return LinkedHashMap<itemno, String>
     */
    public static Map<String, String> getStringValues(String classno) {
        List<LibClassD> list = getLibClassDList(classno);
        Map<String, String> map = new LinkedHashMap<String, String>(list.size());
        for(LibClassD d : list){
            map.put(d.getItemno(), d.getItemname());
        }
        return map;
    }
    
    /**
     * 从缓存取某 classno, itemno 的 LibClassD 对象
     * @param classno 数组编号
     * @param itemno  元素编号
     * @return LibClassD 对象
     */
    public static LibClassD getLibClassDVo(String classno, String itemno) {
    	Map<String, LibClassD> map = getLibClassDMap(classno);
    	if(map == null || !map.containsKey(itemno)) 
    		throw new XLRuntimeException("无匹配参数 LibClassD(classno, itemno)=(" + classno + ", " + itemno + ")");
    	return map.get(itemno);
    }
    
    /**
     * 从缓存取某 classno, itemno 的 LibClassD.itemvalue 值
     * @param classno 数组编号
     * @param itemno  元素编号
     * @return LibClassD.itemvalue 值
     */
    public static String getLibClassDValue(String classno, String itemno) {
    	return getLibClassDVo(classno, itemno).getItemvalue();
    }
    /**
     * 增加对应的LibClassD到缓存
     * @author liheng
     * 2011-9-16
     * @param newLibClassD
     */
    public static void insertLibClassDVo(LibClassD newLibClassD){
    	String classno = newLibClassD.getClassno();
    	String itemno = newLibClassD.getItemno();
    	Map<String,LibClassD> map = getLibClassDMap(classno);
    	if(map == null)
    		throw new XLRuntimeException("无法找到要增加的缓存所在系统数组：LibClassM(classno=" + classno +"）");
    	if(map.containsKey(itemno))
    		throw new XLRuntimeException("要增加的缓存元素：LibClassD(classno,itemno)=(" + classno + "," + itemno + ")已存在，更新失败");
    	map.put(itemno, newLibClassD);
    	LibClassMMap.put(classno, map);
    	logger.debug("增加缓存(classno,itemno)=(" + classno + "," + itemno + "） 完成");
    }
    /**
     * 更新缓存中的相应LibClassD
     * @author liheng
     * 2011-9-16
     * @param newLibClassD
     */
    public static void updateLibClassDVo(LibClassD  newLibClassD){
    	String classno = newLibClassD.getClassno();
    	String itemno = newLibClassD.getItemno();
    	Map<String,LibClassD> map = getLibClassDMap(classno);
    	if(map == null || !map.containsKey(itemno))
    		throw new XLRuntimeException("无法找到要更新的缓存：LibClassD(classno,itemno)=(" + classno + "," + itemno + ")");
    	map.put(itemno,newLibClassD);
    	LibClassMMap.put(classno, map);
    	logger.debug("更新缓存(classno,itemno)=(" + classno + "," + itemno + "） 完成");
    }
    
    /**
     * 删除内存中的相应LibClassD数据
     * 2011-9-16
     * @author liheng
     * @param delLibClassD
     */
    public static void deleteLibClassDVo(LibClassD delLibClassD){
    	String classno = delLibClassD.getClassno();
    	String itemno = delLibClassD.getItemno();
    	Map<String,LibClassD> map = getLibClassDMap(classno);
    	if(map == null || !map.containsKey(itemno))
    		throw new XLRuntimeException("无法找到要删除的缓存：LibClassD(classno,itemno)=(" + classno + "," + itemno + ")");
    	map.remove(itemno);
    	LibClassMMap.put(classno, map);
    	logger.debug("删除缓存(classno,itemno)=(" + classno + "," + itemno + "） 完成");
    }
    
   
    
    /** default constructor */
    public LibClassM() {
    }
    
    
    /** minimal constructor */
    public LibClassM(String classno, String classname, String editby, String edittime) {
        this.classno = classno;
        this.classname = classname;
        this.editby = editby;
        this.edittime = edittime;
    }
    
    /** full constructor */
    public LibClassM(String classno, String classname, String remark, String editby, String edittime) {
        this.classno = classno;
        this.classname = classname;
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
    
    public String getClassno() {
        return this.classno;
    }
    
    public void setClassno(String classno) {
        this.classno = classno;
    }
    
    public String getClassname() {
        return this.classname;
    }
    
    public void setClassname(String classname) {
        this.classname = classname;
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
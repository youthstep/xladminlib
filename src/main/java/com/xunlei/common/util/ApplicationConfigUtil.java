/*
 * Created on Jul 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.common.util;

import com.xunlei.libfun.vo.LibConfig;

import java.io.InputStream;
import java.util.Properties;


/**
 * 用于获取应用程序的部署环境的信息
 * 已经重新配置获取途径。从数据库中获取所有的配置信息而不从属性文件中获取
 * @author jason 
 */
public class ApplicationConfigUtil {
    /**
     * 应用程序的部署路径信息
     */
    public static final String WEBROOT =com.xunlei.common.web.model.WebContext.WEBROOT;
    
    protected static final Properties prop = new Properties();
    
    static {
        try {
            InputStream in = ApplicationConfigUtil.class.getResourceAsStream("/META-INF/commonConfig.properties");
            if(in != null){
            	prop.load(in);
            	in.close();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    /**
     * 是否需要清除临时文件，可通过配置/META-INF/config.properties的clearup-tempdir属性
     * @return 是否需要清除临时文件
     */
    public static boolean isClearupTempdir(){
        //return "true".equalsIgnoreCase(prop.getProperty("clearup-tempdir", "true"));
        return "true".equalsIgnoreCase(LibConfig.getValue("ClearupTempdir","true"));
    }
    

    /**
     * 获取无需过滤的页面路径，默认为index.jsp和login.jsf,可通过配置/META-INF/config.properties的excludepages属性
     * @return 无需过滤的页面路径
     */
    public static String[] getExcludePages(){
        //String pages = prop.getProperty("excludepages", "index.jsp,login.jsf").toLowerCase();
        String pages = LibConfig.getValue("ExcludePages", "index.jsp,login.jsf").toLowerCase();
        return pages.split(",");
    }
    /**
     * 获取无需过滤的目录
     * @return 无需过滤的目录
     */
    public static String[] getExcludeCatalogs(){
    	String catalogs = LibConfig.getValue("ExcludePaths", "").toLowerCase();
    	return catalogs.split(",");
    }
    /**
     * 获取需要进行额外过滤的路径
     * @return
     */
    public static String[] getFilterpaths(){
    	String catalogs = LibConfig.getValue("FilterPaths", "").toLowerCase();
    	return catalogs.split(",");
    }
    
    /**
     * 是否保存cookies
     * @return 是否保存cookies
     */
    public static boolean isCookies() {
		return "true".equalsIgnoreCase(LibConfig.getValue("IsCookies", "false"));
	}
    

    /**
     * 获得默认的css样式
     * @return 默认的css样式
     */
    public static String getCssPath() {
		return LibConfig.getValue("CssPath", "/css/blue/");
	}


    /**
     * 顶部邮件提示更新时间间隔，返回的为毫秒
     * @return
     */
    public static int getMailupdateInterval(){
        String v=LibConfig.getValue("MailUpdateInterval");
        return Integer.valueOf(v)*1000;
    }
    
    /**
     * 获取平台编号在系统数组维护中的ClassNo，可通过配置/META-INF/config.properties的flatclassno属性
     * 如果没有，则 flatclassno=""
     * @return 平台编号在系统数组维护中的ClassNo
     */
    public static String getFlatclassno(){
        return LibConfig.getValue("FlatClassNo");
    }
    
    /**
     * 获取与平台编号有关的数据表名称，可通过配置/META-INF/config.properties的flattable属性
     * 如果没有，则 flattable=""
     * @return 与平台编号有关的数据表名称
     */
    public static String getFlattable(){
        return LibConfig.getValue("FlatTable");
    }
    
    /**
     * 获取与平台编号有关的数据表中的平台ID字段名称，可通过配置/META-INF/config.properties的flatid属性
     * 如果没有，则 flatid=""
     * @return 与平台编号有关的数据表中的平台ID字段名称
     */
    public static String getFlatid(){
        return LibConfig.getValue("FlatId");
    }
    
    /**
     * 获取与平台编号有关的数据表中的平台名称字段，可通过配置/META-INF/config.properties的flatname属性
     * 如果没有，则 flatname=""
     * @return 与平台编号有关的数据表中的平台名称字段
     */
    public static String getFlatname(){
        return LibConfig.getValue("FlatName");
    }
    
    /**
     * 获取与平台编号有关的数据表中的拼音简码字段，可通过配置/META-INF/config.properties的flatindex属性
     * 如果没有，则 flatindex=""
     * @return 与平台编号有关的数据表中的拼音简码字段
     */
    public static String getFlatindex(){
        return LibConfig.getValue("FlatIndex");
    }
    
    /**
     * 获取与平台编号有关的数据表中的数据角色字段，可通过配置/META-INF/config.properties的recnofield属性
     * 如果没有，则 recnofield=""
     * @return 与平台编号有关的数据表中的数据角色字段
     */
    public static String getRecnofield(){
        return LibConfig.getValue("RecNoField");
    }
    
    /**
     * 获取上传文件的根目录，可通过配置/META-INF/config.properties的root_files_path属性
     */
    public static String getRootFilePath(){
        return LibConfig.getValue("RootFilesPath");
    }
    
    /**
     * 获取上传文件的根目录对应的http地址
     */
    public static String getFilesDomain(){
        return LibConfig.getValue("FilesDomain");
    }
    /**
     * 获得指定key的指。
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getValue(String key,String defaultValue){
        return prop.getProperty(key,defaultValue);
    }

    /**
     * 是否显示导航条
     * @return
     */
    public static boolean getShownavi(){
        return Boolean.parseBoolean(LibConfig.getValue("ShowNavi","true"));
    }
    
    /**
     * 功能访问日志保存天数
     * @return
     */
    public static int getFunctionlogsStoreDay(){
    	return Integer.parseInt(LibConfig.getValue("LogStoreDays"));
    }
    
    /**
     * 最后删除日志时间
     * @return
     */
    public static String getFunctionlogsDelDate(){ 
    	return LibConfig.getValue("LogDelDate");
    	
    }

    public static String getVerifyCodeHost(){
        return LibConfig.getValue("VerifyCodeHost");
    }
    public static String getVerifyCodeImage(){
        return LibConfig.getValue("VerifyCodeImage");
    }
    
}

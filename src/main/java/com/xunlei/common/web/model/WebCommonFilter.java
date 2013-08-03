/*
 * WebCommonFilter.java
 *
 * Created on 2006年11月11日, 下午1:28
 */

package com.xunlei.common.web.model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xunlei.common.plugin.FilterPluginable;
import com.xunlei.common.plugin.FilterPluginable.ResultType;
import com.xunlei.common.plugin.ThrowableHanderPluginable;
import com.xunlei.common.plugin.UserCookiePluginable;
import com.xunlei.common.util.ApplicationConfigUtil;
import com.xunlei.common.util.Constants;
import com.xunlei.common.util.StringTools;
import com.xunlei.common.util.UserCookie;
import com.xunlei.common.web.WebApplicationContextUtil;
import com.xunlei.libfun.bo.IUsersBo;
import com.xunlei.libfun.vo.UserInfo;

/**
 * 用于中转请求，统一处理异常等。<br>
 * 支持中转请求插件，需实现FilterPluginable接口<br>
 * 支持异常处理插件，需实现ThrowableHanderPluginable接口<br>
 * @author  kamhung 
 */

public class WebCommonFilter implements Filter {
    
    private static org.apache.log4j.Logger log = null;
    
    private String webroot = null;

    private String exceptionTemplate=null;//用于保存异常模板
    
    private String[] excludepages = new String[0];
    
    private String[] excludecatalogs = new String[0];
    
    private Map<String,String> filterpathsMap=new HashMap<String,String>();
    
    private final List<FilterPluginable> filterplugins = new ArrayList<FilterPluginable>(1);
    
    private final List<UserCookiePluginable> userCookiePlugins = new ArrayList<UserCookiePluginable>(1);
    
    private final List<ThrowableHanderPluginable> throwableplugins = new ArrayList<ThrowableHanderPluginable>(1);
    protected static Log logger=LogFactory.getLog(WebCommonFilter.class);
    public WebCommonFilter() {
    }
    
    private void initFilterPluginable(){
        ServiceLoader<FilterPluginable> loader = ServiceLoader.load(FilterPluginable.class);
        Iterator<FilterPluginable> it = loader.iterator();
        while(it.hasNext()){
            filterplugins.add(it.next());
        }
    }
    
    private void initThrowableHanderPluginable(){
        ServiceLoader<ThrowableHanderPluginable> loader = ServiceLoader.load(ThrowableHanderPluginable.class);
        Iterator<ThrowableHanderPluginable> it = loader.iterator();
        while(it.hasNext()){
            throwableplugins.add(it.next());
        }
    }
    
    private void initUserCookiePluginable(){
        ServiceLoader<UserCookiePluginable> loader = ServiceLoader.load(UserCookiePluginable.class);
        Iterator<UserCookiePluginable> it = loader.iterator();
        while(it.hasNext()){
        	userCookiePlugins.add(it.next());
        }
    }
    
    private ResultType doBeforeProcessing(String servletpath, HttpServletRequest httprequest, ServletResponse response)
    throws IOException, ServletException {
        if(!servletpath.endsWith(".jsf") && !servletpath.endsWith(".jsp") && !this.filterpathsMap.containsKey(servletpath) ) return ResultType.GOON;
        if(servletpath.indexOf("/servlet/DisplayChart") >= 0) return ResultType.GOON;
        if(servletpath.endsWith("/plaf/exception.jsp")) return ResultType.GOON;
        if(servletpath.indexOf("ajax4jsf") >= 0) return ResultType.GOON;
        for(String s : excludepages) if(servletpath.indexOf(s) >= 0) return ResultType.GOON;
        for(String s : excludecatalogs) if(!s.isEmpty() && servletpath.startsWith(s)) return ResultType.GOON;
        ResultType result = ResultType.GOON;
        for(FilterPluginable plugin : filterplugins) {
            result = plugin.doBeforeProcessing(webroot, httprequest, (HttpServletResponse)response);
            if(result != ResultType.GOON) break;
        }
        if(result != ResultType.GOON) return result;
        final HttpSession session = httprequest.getSession(true);
        UserInfo userInfo=(UserInfo)session.getAttribute("userinfo");
        if (userInfo==null) {
			if (ApplicationConfigUtil.isCookies()) {
				if(filterWithCookie(httprequest, (HttpServletResponse) response)==ResultType.ABORT) return ResultType.ABORT;
			} else {
                if(servletpath.indexOf("index.jsf")<0 && servletpath.indexOf("index.jsp")<0){
                    HttpServletResponse resp=(HttpServletResponse)response;
                    resp.getWriter().write("<script type='text/javascript'>top.location='"+httprequest.getContextPath()+"/plaf/invaliduserinfo.html'</script>");
                    return ResultType.ABORT;
                    //throw new XLRuntimeException(readSmallFile(new File(webroot + "/plaf/invaliduserinfo.html")));
                }
			}
		}
        return ResultType.GOON;
    }
    
    private ResultType filterWithCookie(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		boolean isPluginSession = false;
		boolean isCommonSession = false;
		if (userCookiePlugins.size() > 0) {
			for (UserCookiePluginable plugin : userCookiePlugins) {
				plugin.setRequest(request);
				plugin.setResponse((HttpServletResponse) response);
				isPluginSession = plugin.filterCookie();
				if (isPluginSession == false)
					break;
			}
		} else {
			isPluginSession = true;
		}
		if (!isPluginSession || !isCommonSession) {
            //对index.jsp处理完cookie之后进行特殊处理使其不跳转到过期页。而是在完成cookie登录后继续index.jsp的流程，完成自动登录跳转
            final String servletpath = request.getServletPath().toLowerCase();
            if(servletpath.indexOf("index.jsf")<0 && servletpath.indexOf("index.jsp")<0){
                //throw new XLRuntimeException(readSmallFile(new File(webroot + "/plaf/invaliduserinfo.html")));
                HttpServletResponse resp=(HttpServletResponse)response;
                resp.getWriter().write("<script type='text/javascript'>top.location='"+request.getContextPath()+"/plaf/invaliduserinfo.html'</script>");
                return ResultType.ABORT;
                
            }
		}
        return ResultType.GOON;
	}
    
    @SuppressWarnings("unused")
	private void doAfterProcessing(ServletRequest request, ServletResponse response)
    throws IOException, ServletException {
        //do nothing
    }
    /**
     *中转请求，统一处理异常等。
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        final HttpServletRequest httprequest = (HttpServletRequest)request;
        final String servletpath = httprequest.getServletPath().toLowerCase();
        try {
            if(doBeforeProcessing(servletpath, httprequest, response) == ResultType.ABORT) return;
        } catch(Throwable t) {
            sendProcessingMessage(t, response);
            return;
        }
        try {
            chain.doFilter(request, response);
        } catch(Throwable t) {
            t.printStackTrace();
            log.error("unkown error : ", t);
            boolean  result = true;
            for(ThrowableHanderPluginable plugin : throwableplugins) {
                result = plugin.sendProcessingException(webroot, t, httprequest, (HttpServletResponse)response);
                if(!result) break;
            }
            if(result) {
                //这里只有一个页面处理异常，何不使用缓存呢？
                //sendProcessingException(t, response, new File(webroot+"/plaf/exception.html"));
                sendProcessingMessageFromFile(t,response,webroot,httprequest.getContextPath());
            }
            return;
        }
        //doAfterProcessing(request, response);
    }

    
    private void sendProcessingMessageFromFile(Throwable t, ServletResponse response,String webroot,String contextpath) {

        try {
            if(exceptionTemplate==null){
                exceptionTemplate = readSmallFile(new File(webroot+"/plaf/exception.html"));
                exceptionTemplate=exceptionTemplate.replaceAll("\\$\\{appContextPath\\}", contextpath);
            }

            String content=exceptionTemplate;
            int index1 = content.indexOf("${message}");
            String header = content.substring(0, index1);
            int index2 = content.indexOf("${stackTrace}");
            String middle = content.substring(index1 + "${message}".length(), index2);
            String footer = content.substring(index2 + "${stackTrace}".length());
            content =header + getMessage(t) + middle + getStackTrace(t) + footer;

            response.setContentType("text/html");
            response.setCharacterEncoding("GBK");
            PrintStream ps = new PrintStream(response.getOutputStream());
            PrintWriter pw = new PrintWriter(ps);
            pw.print(content);
            pw.close();
            ps.close();
            response.getOutputStream().close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void sendProcessingMessage(Throwable t, ServletResponse response) {
        try {
            response.setContentType("text/html");
            response.setCharacterEncoding("GBK");
            PrintStream ps = new PrintStream(response.getOutputStream());
            PrintWriter pw = new PrintWriter(ps);
            pw.print(t.getMessage());
            pw.close();
            ps.close();
            response.getOutputStream().close();
        } catch(Exception ex){
        }
    }
    
    private void sendProcessingException(Throwable t, ServletResponse response, File file) {
        try {
            String content = readSmallFile(file);
            int index1 = content.indexOf("${message}");
            String header = content.substring(0, index1);
            int index2 = content.indexOf("${stackTrace}");
            String middle = content.substring(index1 + "${message}".length(), index2);
            String footer = content.substring(index2 + "${stackTrace}".length());
            content =header + getMessage(t) + middle + getStackTrace(t) + footer;
            
            response.setContentType("text/html");
            response.setCharacterEncoding("GBK");
            PrintStream ps = new PrintStream(response.getOutputStream());
            PrintWriter pw = new PrintWriter(ps);
            pw.print(content);
            pw.close();
            ps.close();
            response.getOutputStream().close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void destroy() {
    }
    /**
     *初始化系统常量
     *初始化系统常量组
     *初始化中转请求插件
     *初始化异常处理插件
     */
    public void init(FilterConfig filterConfig) {
        if(filterConfig != null){
            //System.out.println("init FilterConfig");
            this.webroot = filterConfig.getServletContext().getRealPath("").replace('\\', '/');
            configlog(null);
            log = org.apache.log4j.Logger.getLogger(WebCommonFilter.class);
            this.excludepages = ApplicationConfigUtil.getExcludePages();
            this.excludecatalogs = ApplicationConfigUtil.getExcludeCatalogs();
            String[] fp=ApplicationConfigUtil.getFilterpaths();
            if(fp!=null){
                for(String s:fp){
                    this.filterpathsMap.put(s, null);
                }
            }
        }
        initFilterPluginable();
        initThrowableHanderPluginable();
        initUserCookiePluginable();
    }
    
    /**
     * 配置日志
     */
    private void configlog(String contextpath) {        
        if(org.apache.log4j.Logger.getRootLogger().getAllAppenders().hasMoreElements()) return;

        new File(ApplicationConfigUtil.WEBROOT+"logs").mkdirs();
        Properties prop =new Properties();
        if(contextpath == null || contextpath.length() == 0) {
            contextpath ="/rootlog";
        } else if(contextpath.endsWith("/") || contextpath.endsWith("//")){
            contextpath =contextpath.substring(0, contextpath.length() -1);
        }
        System.out.println("load default log4j config.");
        prop.put("log4j.rootLogger", "WARN,xlweb");
        prop.put("log4j.appender.xlweb", "org.apache.log4j.RollingFileAppender");
        prop.put("log4j.appender.xlweb.File", ApplicationConfigUtil.WEBROOT+ "logs/"+contextpath+".log");
        prop.put("log4j.appender.xlweb.Append", "true");
        prop.put("log4j.appender.xlweb.MaxFileSize", "8MB");
        prop.put("log4j.appender.xlweb.MaxBackupIndex", "3");
        prop.put("log4j.appender.xlweb.layout", "org.apache.log4j.PatternLayout");
        prop.put("log4j.appender.xlweb.layout.ConversionPattern", "%d{[yyyy-MM-dd HH:mm:ss.SSS]}:[%p]  %l %m%n");
        org.apache.log4j.PropertyConfigurator.configure(prop);
    }
    
    private static String getMessage(Throwable t) {
        if(t instanceof ServletException) {
            ServletException se =(ServletException)t;
//            if(se.getRootCause() instanceof FacesException) {
//                FacesException fe =(FacesException)se.getRootCause();
//                if(fe.getCause() != null) {
//                    return StringTools.escapeHtml(fe.getCause().getCause() == null ? fe.getCause().getMessage() : fe.getCause().getCause().getMessage());
//                } else {
//                    return StringTools.escapeHtml(fe.getMessage());
//                }
//            }
        }
        Throwable buf = t;
        while(buf.getCause() != null) {
            buf =buf.getCause();
        }
        return StringTools.escapeHtml(buf.getMessage());
    }
    
    private static String getStackTrace(Throwable t) {
        String stackTrace = "";
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = org.apache.commons.lang.StringEscapeUtils.escapeHtml(sw.getBuffer().toString());
        } catch(Exception ex) {}
        return stackTrace;
    }
    
    //读文件
    private static String readSmallFile(File file) throws IOException {
        if(!file.isFile()) return "not found file (\""+file.getName()+"\").";
        StringBuilder sb=new StringBuilder((int)file.length() + 300);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
        String str =null;
        while((str = br.readLine()) != null){
            sb.append(str+"\n");
        }
        br.close();
        return sb.toString();
    }
}

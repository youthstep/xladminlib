package com.xunlei.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供服务端验证的常用方法
 * @author 刘凌
 */
public class Validator {
	private final static Pattern pcharanddigit = Pattern.compile("[0-9a-zA-Z]+");
	private final static Pattern pdatey_m_d = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");
	private final static Pattern pip4 = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");//ip地址匹配192.168.1.3
	private final static Pattern pdomain = Pattern.compile("[a-zA-Z0-9-\\u4e00-\\u9fa5]+(.[a-zA-Z0-9-]+)+");
	private final static Pattern pinteger = Pattern.compile("[+-]?\\d+");
	private final static Pattern pnumber = Pattern.compile("\\d+");
	private final static Pattern purl = Pattern.compile("(http:\\/\\/)?+[a-z0-9-]+(\\.[a-z0-9-]+)+(\\/[\\w-\\.\\\\/?%&=]*)?");
	private final static Pattern pemail = Pattern.compile("^[\\w]+[\\w-\\.]*\\@[\\w]+[\\w\\.-]*[\\w]+$");
	private final static Pattern pserialno = Pattern.compile("[a-z0-9-_]+");
	private final static Pattern pdateymd = Pattern.compile("\\d{8}");
	
	private final static String DOMAINS =".com.cn|.net.cn|.org.cn|.gov.cn|.com|.net|.tv|.gd|.org|.cc|.vc|.mobi|.cd|.info|.name|.asia|.hk|.me|.la|.sh|.biz|.li|.kr|.in|.us|.io|.ac.cn|.bj.cn|.sh.cn|.tj.cn|.cq.cn|.he.cn|.sx.cn|.nm.cn|.ln.cn|.jl.cn|.hl.cn|.js.cn|.zj.cn|.ah.cn|.fj.cn|.jx.cn|.sd.cn|.ha.cn|.hb.cn|.hn.cn|.gd.cn|.gx.cn|.hi.cn|.sc.cn|.gz.cn|.yn.cn|.xz.cn|.sn.cn|.gs.cn|.qh.cn|.nx.cn|.xj.cn|.tw.cn|.hk.cn|.mo.cn|.cn";
	/**
     * 是否整数,可以带正负号
     * @param s 目标字符串
     * @return 是否整数
     */
    public static boolean isInteger(String s){
    	if (isEmpty(s)) {
			return false;
		}    	
		Matcher m = pinteger.matcher(s);
		return m.matches();
    }
	/**
	 * 是否是数字
	 * 
	 * @param s 目标字符串
	 * @return 是否是数字
	 */
	public static boolean isNumber(String s) {		
		if (isEmpty(s)) {
			return false;
		}    	
		Matcher m = pnumber.matcher(s);
		return m.matches();
	}	
	/**
	 * 是否是url地址
	 * 
	 * @param str http://www.xunlei.com url地址
	 * @return 是否是url地址
	 */
	public static boolean isURL(String str) {
		if (isEmpty(str)) {
			return false;
		}
		Matcher m = purl.matcher(str);
		return m.matches();
	}
	/**
	 * 是否是Email地址
	 * 
	 * @param str liuling@xunlei.com(可以含有横线，点，下划线) Email地址
	 * @return 是否是Email地址
	 */
	public static boolean isEmail(String str) {
		if (isEmpty(str)) {
			return false;
		}
		str = str.toLowerCase().trim();
				
		Matcher m = pemail.matcher(str);
		return m.matches();
	}	
	/**
	 * 校验编号, 只能是数字,字母, 下划线, 减号
	 * 
	 * @param str 目标字符串 例如：http://www.xunlei.com
	 * @return 是否是编号
	 */
	public static boolean isSerialNo(String str) {
		if (isEmpty(str)) {
			return false;
		}
		str = str.toLowerCase().trim();
		
		Matcher isSerialNo = pserialno.matcher(str);
		return isSerialNo.matches();
	}
	/**
	 * 是否含有非法字符
	 * 
	 * @param str 目标字符串 
	 * @return 是否含有非法字符
	 */
	public static boolean isForbid(String str) {
		if (isEmpty(str)) {
			return false;
		}
		str = str.toLowerCase().trim();
		String[] forbids = { "%", "\\", "'", "*" };
		for (String s : forbids) {
			if (str.indexOf(s) > -1) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断日期字符串是否是yyyyMMdd的格式
	 * @param s 日期字符串
	 * @return 格式是否符合
	 */
	public static boolean isDateYMD(String s){
		if (isEmpty(s)) {
			return false;
		}
		
		Matcher m = pdateymd.matcher(s);
		return m.matches();
    }
	/**
	 * 判断日期字符串是否是yyyy-MM-dd的格式
	 * @param s 日期字符串
	 * @return 格式是否符合
	 */
	public static boolean isDateY_M_D(String s){
		if (isEmpty(s)) {
			return false;
		}
		Matcher m = pdatey_m_d.matcher(s);
		return m.matches();
    }
	public static boolean isCharAndDigit(String s){
		if (isEmpty(s)) {
			return false;
		}		
		Matcher m = pcharanddigit.matcher(s);
		return m.matches();
	}
	public static boolean isEmpty(String s){
		if(null == s || s.trim().equals("")){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isNotEmpty(String s){
		return !isEmpty(s);
	}

    /**
     * 判断字符串是不是都为空
     * @param str
     * @return 只要有不空的字符串就返回false
     */
    public static boolean isAllEmpty(String... str){
        if(str==null){
            return true;
        }
        for(String s:str){
            if(isNotEmpty(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是所有字符串都不为空
     * @param str 字符串
     * @return 只要有为空的就返回false
     */
    public static boolean isAllNotEmpty(String... str){
        if(str==null){
            return false;
        }
        for(String s:str){
            if(isEmpty(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断某个字符串长度是不是在一个范围内（包含边界值）。
     * @param str 字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return
     */
    public static boolean isStringBetween(String str,int minLength,int maxLength){
        if(str==null){
            return false;
        }
        if(str.length()>=minLength && str.length()<=maxLength){
            return true;
        }
        return false;
    }
	/**
     * 是不是IP地址
     * @param s 目标字符串
     * @return 是不是IP地址
     */
    public static boolean isIP(String s){    	
        Matcher m = pip4.matcher(s);
        return m.matches();
    }
    public static boolean isDomain(String s) {
		if(isEmpty(s)){
			return false;
		}
		Matcher m = pdomain.matcher(s);
		return m.matches();
	}
    public static boolean isNotDomain(String s){
    	return !isDomain(s);
    }
    /**
	 * 是否是主域名如 xunlei.com
	 * 
	 * @param str 目标字符串
	 * @return 是否是主域名
	 */
	public static boolean isMainDomain(String str) {		
		if(null == str || ("").equals(str.trim())){
			return false;
		}
		String[] domains = DOMAINS.split("\\|");
		for(String s : domains){
			if (null != s && !s.trim().equals("") && str.indexOf(s) > 0) {
				str = str.replace(s, "");
				if(Validator.isSerialNo(str)){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	public  static void main(String[]a){
		String s = "http://sss.com.cn.cn.cn";
		System.out.println(Validator.isDomain("qushua.com"));

        System.out.println(isAllNotEmpty("","","d"));
	}
}

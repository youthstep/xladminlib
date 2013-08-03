/**
 * @description
 * @filename UserCookie.java
 * @author zhangshuai
 * @createtime May 23, 2008 4:40:11 PM
 */
package com.xunlei.common.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunlei.common.util.base64.BASE64Decoder;
import com.xunlei.common.util.base64.BASE64Encoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;


@SuppressWarnings("unchecked")
/**
 * 存放用户的Cookie信息。
 * @author 刘凌
 */
public class UserCookie<T> {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String cookiePath = "/";
	private String cookieDomain = null;
    protected static final Logger logger=Logger.getLogger(UserCookie.class);

	public UserCookie(HttpServletRequest request, HttpServletResponse response,
			String cookiePath, String cookieDomain) {
		this.request = request;
		this.response = response;
		if (cookiePath != null)
			this.cookiePath = cookiePath;
		this.cookieDomain = cookieDomain;
	}

	public void addC(String name, String value, int maxage) {
		Cookie cookies = new Cookie(name, value);
		cookies.setPath(cookiePath);
		if (maxage > 0) {
			cookies.setMaxAge(maxage);
		} else {
			cookies.setMaxAge(-1);
		}
		if (StringTools.isNotEmpty(cookieDomain)) {
			cookies.setDomain(cookieDomain);
		}
		this.response.addCookie(cookies);
	}

	public void addDESC(String name, String value, int maxage) {
		try {
			BASE64Encoder base64Encoder = new BASE64Encoder();
			value = StringTools.encodeURL(base64Encoder.encode(value.getBytes()));
			Cookie cookies = new Cookie(name, value);
			if (maxage > 0) {
				cookies.setMaxAge(maxage);
			} else {
				cookies.setMaxAge(-1);
			}
			cookies.setPath(cookiePath);
			if (StringTools.isNotEmpty(cookieDomain)) {
				cookies.setDomain(cookieDomain);
			}
			this.response.addCookie(cookies);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void addCByArrays(String[][] values, int maxage) {
		for (String[] value : values) {
			addDESC(value[0], value[1], maxage);
		}
	}

	public void addCByObject(Object obj, int maxage) {
		try {
			String fname = "";
			Method method = null;
			Class<T> clazz = (Class<T>) obj.getClass();
            String cname=clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();//用于保存校验段
            //StringBuilder totalString=new StringBuilder();//用于保存需要校验的数据，这里时所有字段和
            //20081215由于发现在一些机器上顺序会出现不同。所以使用数组进行排序后再生成校验字段
            List<String> totalStringList=new ArrayList<String>();
            String fvalue="";
			for (Field field : clazz.getDeclaredFields()) {
				if (Modifier.isFinal(field.getModifiers())
						|| Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				fname = field.getName();
				method = clazz.getDeclaredMethod(capitalize(field));
//				System.out.println(fname+":"+getValues(method.invoke(obj)));
                fvalue=getValues(method.invoke(obj));
                //totalString.append(fvalue);
                totalStringList.add(fvalue);
				addDESC(fname, fvalue, maxage);
			}
            //加入校验字段，防止客户端篡改！！！      by IceRao
            String strs=StringTools.ArrayToSortString(totalStringList);
            logger.debug("before md5 str:"+strs);
            String hashed=MD5Hash.hash(strs);
            addC(cname,hashed,maxage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	public void addCByObject(Object userInfo, int maxage, boolean isBase,
//			Object extObj) {
//		try {
//			if (isBase) {
//				String fname = "";
//				Method method = null;
//				Class<T> clazz = (Class<T>) userInfo.getClass();
//				for (Field field : clazz.getDeclaredFields()) {
//					if (Modifier.isFinal(field.getModifiers())
//							|| Modifier.isStatic(field.getModifiers())) {
//						continue;
//					}
//					fname = field.getName();
//					method = clazz.getDeclaredMethod(capitalize(field));
//					// System.out.println(fname+":"+getValues(method.invoke(userInfo)));
//					addDESC(fname, getValues(method.invoke(userInfo)), maxage);
//				}
//			} else {
//				if (extObj != null) {
//					for (UserCookiePluginable plugin : userCookiePlugin) {
//						String[][] values = plugin.addCookie(extObj);
//						addCByArrays(values, maxage);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public Object getCByobject(Class clazz) {
		try {
			String fname = "";
			String fvalue = null;
			Method method = null;
            String cname=clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();//用于保存校验段
            //StringBuilder totalString=new StringBuilder();//用于保存需要校验的数据，这里是所有字段和,
            //20081215由于发现在一些机器上顺序会出现不同。所以使用数组进行排序后再生成校验字段
            List<String> totalStringList=new ArrayList<String>();
			BASE64Decoder base64Decoder = new BASE64Decoder();
			T indata = (T) clazz.newInstance();
			Cookie cookies[] = request.getCookies();
			Object[] in = new Object[1];
			boolean ishavecookie = false;
			if (cookies != null && cookies.length > 0) {
				for (Field field : clazz.getDeclaredFields()) {
					ishavecookie = false;
					if (Modifier.isFinal(field.getModifiers())
							|| Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					fname = field.getName();
					fvalue = "";
					for (int i = 0; i < cookies.length; i++) {
						if (fname.equals(cookies[i].getName())) {
							ishavecookie = true;
							if (!cookies[i].getValue().isEmpty()) {
								fvalue = new String(base64Decoder.decodeBuffer(StringTools.decodeURL(cookies[i].getValue())));
                                //totalString.append(fvalue);
                                totalStringList.add(fvalue);
							}
							break;
						}
					}
					if(!ishavecookie) continue;
					if (fvalue != null) {
						method = clazz.getDeclaredMethod("set"
								+ capitalize(fname), field.getType());
						if (field.getType() == String.class) {
							method.invoke(indata, fvalue);
						} else if (field.getType() == Boolean.TYPE) {
							method.invoke(indata, Boolean.valueOf(fvalue));
						} else if (field.getType() == Byte.TYPE) {
							method.invoke(indata, Byte.valueOf(fvalue));
						} else if (field.getType() == Short.TYPE) {
							method.invoke(indata, Short.valueOf(fvalue));
						} else if (field.getType() == Integer.TYPE) {
							method.invoke(indata, Integer.valueOf(fvalue));
						} else if (field.getType() == Long.TYPE) {
							method.invoke(indata, Long.valueOf(fvalue));
						} else if (field.getType() == Float.TYPE) {
							method.invoke(indata, Float.valueOf(fvalue));
						} else if (field.getType() == Double.TYPE) {
							method.invoke(indata, Double.valueOf(fvalue));
						} else if(field.getType() == String[].class){
                            String[] values;
                            if(StringTools.isEmpty(fvalue)){
                                values=new String[0];
                            }
                            else{
                                values = fvalue.split("\\|");
                            }
							in[0] = values;
							method.invoke(indata,in);
						}
					}
				}
                //获得校验值
                for(int i=0;i<cookies.length;i++){
                    if(cname.equals(cookies[i].getName())){
                        fvalue=new String(cookies[i].getValue());
                        break;
                    }
                }
			}
            //对数据进行校验，失败时返回null
            if(StringTools.isEmpty(fvalue)){
                return null;
            }            
            String strs=StringTools.ArrayToSortString(totalStringList);
            logger.debug("before md5 str:"+strs);
            String hashed=MD5Hash.hash(strs);
            if(!fvalue.equals(hashed)){
                return null;
            }
			return indata;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCookie(String name, boolean isDecoder) {
		Cookie cookies[] = request.getCookies();
		Cookie sCookie = null;
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			if (cookies != null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					sCookie = cookies[i];
					if (sCookie.getName().equals(name)) {
						if (isDecoder) {
							return new String(base64Decoder
									.decodeBuffer(sCookie.getValue()));
						} else {
							return sCookie.getValue();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String capitalize(Field field) {
		String pre = "get";
		String name = field.getName();
		if (field.getType() == Boolean.TYPE) {
			pre = "is";
		}
		return pre + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private static String capitalize(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

    /**
     * 删除一个对象的cookie使其失效。简单的办法就是删除其校验段
     * @param clazz
     */
    public void delObject(Class clazz){
        String cname=clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();//用于保存校验段
        Cookie cookie=new Cookie(cname, null);
        cookie.setMaxAge(0);
        cookie.setValue(null);
        cookie.setPath(this.cookiePath);
        this.response.addCookie(cookie);
    }

	private static String getValues(Object obj) {
		if (obj != null) {
			if (obj instanceof String[]) {
				StringBuilder sb = new StringBuilder();
				String[] values = (String[]) obj;
				for (String s : values) {
					sb.append(s).append("|");
				}
				if (sb.length() > 0) {
					return sb.substring(0, sb.length() - 1);
				}
			} else {
				return obj.toString();
			}
		}
		return "";
	}

	public static void main(String[] args) {
		try {
			String value = "admin";
			BASE64Encoder base64Encoder = new BASE64Encoder();
			value = base64Encoder.encode(value.getBytes());
			System.out.println(value);
			BASE64Decoder base64Decoder = new BASE64Decoder();
			value = new String(base64Decoder.decodeBuffer("YzM2ZjI2NTQzZGZlY2M5Yzk1MTFmNGY2YTMxY2RlMWI="));
			System.out.println(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.xunlei.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 此类的主要作用是对数据进行hash。提供给密码保护或者数据校验用。
 * 创建时间：9:58:47
 * @author IceRao
 */
public class MD5Hash {

    /**
     * 返回经过MD5编码的串
     * @param wanthashstr 源字符串
     * @param key 加密密钥
     * @param encode 字符编码方式，例如GBK、UTF-8
     * @return 经过编码后的字符串
     */
    public static String hash(String wanthashstr,String key,String encode){
        char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String digestStr = null;
        try {
            String input = wanthashstr + key;
            byte[] bytes = input.getBytes(encode);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] rs = digest.digest(bytes);
            int j = rs.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < rs.length; i++) {
                byte b = rs[i];
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            digestStr = new String(chars);
        } catch (NoSuchAlgorithmException ex) {
        } catch (UnsupportedEncodingException ex) {
        }

        return digestStr;
    }
    
    public static String encryptPwd(String oriPwd){
        String key="";//this is xllib,erICe&8derrao
        return hash(oriPwd,key,"GBK");
    }

	/**
	 * 获得数据的hash值
	 * @param str 目标数据
	 * @return hash值
	 */
    public static String hash(String str) {
       final String key = "you know thunder?or you know monkey.etc"; //此字符串相当于加密用的串，时数据校验结果不会被猜测。
       return hash(str,key,"UTF-8");       
    }

    public static void main(String[] args) {
        System.out.println(hash("ice"));
        System.out.println(encryptPwd("ice"));
    }
}

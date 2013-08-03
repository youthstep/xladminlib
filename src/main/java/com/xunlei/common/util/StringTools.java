/*
 * StringTools.java
 *
 * Created on 2007年8月22日, 下午4:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.util.base64.BASE64Decoder;
import com.xunlei.common.util.base64.BASE64Encoder;
/**
 * ChartUtils.java
 * if (type.equalsIgnoreCase("timeseries")) {
 * chart = ChartFactory.createTimeSeriesChart("", xAxis, yAxis,dataset, legend, true, false);
 * XYLineAndShapeRenderer renderlocal = (XYLineAndShapeRenderer)chart.getXYPlot().getRenderer();
 * renderlocal.setItemLabelGenerator(new StandardXYItemLabelGenerator());
 * renderlocal.setShapesVisible(true);
 * renderlocal.setItemLabelsVisible(true);
 *
 * @author 张金雄
 */
public final class StringTools {

    /**
     * 判断某个字符串是否包含在某个数组中。如果数组为null则返回false
     * @param str
     * @param array
     * @return
     */
    public static boolean isContainsString(String str, String[] array) {
        if(array==null){
            return false;
        }
        for(String s:array){
            if(s.equals(str)){
                return true;
            }
        }
        return false;
    }


    
    private StringTools(){}
    
    private static final char hex[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static final Format numformat =new DecimalFormat("#.##");
    private static final String zeros="00000000000000000000";
    
    /**
     * 需要进行过滤并替换的sql字符
     */
    private static final String[][] sqlhandles={{"'","''"},{"\\\\","\\\\\\\\"}};

    /**
     *加密一字符串
     */
    public static String encrypt(String s) {
        return variance(s, 2006, true);
    }
    /**
     *解密一字符串
     */
    public static String decrypt(String s) {
        return variance(s, 2006, false);
    }
     
    private static String variance(final String s,int key,final boolean isEncrypt){
        final int seedA = 3467;
        final int seedB = 1239;
        byte ps,pr;
        try{
            byte[] src = isEncrypt ? s.getBytes() : new BASE64Decoder().decodeBuffer(s);
            byte[] buf = new byte[src.length];
            for (int i=0;i<src.length;i++){
                ps = src[i];
                pr = (byte)(ps ^ (key >>> 8));
                buf[i] = pr;
                if (isEncrypt)
                    key = (pr + key) * seedA + seedB;
                else
                    key = (ps + key) * seedA + seedB;
            }
            return isEncrypt ? new BASE64Encoder().encode(buf) : new String(buf);
        } catch(Exception e){
            throw new XLRuntimeException(e);
        }
    }
   
    public static String listingString(Object data) {
        return listingString(data, true);
    }
    
    public static String listingString(Object data, boolean snapped) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(data.getClass().getSimpleName()).append("[");
        try {
            boolean flag = false;
            boolean isstring = true;
            Object obj = null;
            String str = "";
            for(java.lang.reflect.Method m : data.getClass().getDeclaredMethods()){
                if((m.getName().startsWith("get") || m.getName().startsWith("is"))
                && m.getParameterTypes().length == 0){
                    int l = m.getName().startsWith("get") ? 3 : 2;
                    obj = m.invoke(data);
                    if(snapped && obj == null) continue;
                    isstring = obj instanceof String;
                    if(!isstring && snapped) {
                        if(obj instanceof Number && ((Number)obj).intValue() == 0 ) continue;
                        if(obj instanceof Boolean && ((Boolean)obj) == false ) continue;
                    }
                    str = isstring ? ("\"" + obj + "\"") : String.valueOf(obj);
                    if(flag) sb.append(", ");
                    sb.append(m.getName().substring(l).toLowerCase()).append("=").append(str);
                    flag = true;
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String subString(String t, int size){
        if(t == null) return null;
        int hansize = size*3/2;
        int len = hansize;
        if(t.length() > size) {
            int p = 0;
            for(int i=0; i < hansize && i< t.length(); i++) {
                if(t.charAt(i) > 127) p++;
            }
            len -= p*2/3;
            if(len < size) len = size;
            if(t.length() <= len) return t;
            return t.substring(0, len)+"...";
        }
        return t;
    }
  
    /**
     * 返回全局唯一序列号，模拟Sql Server的newid()函数功能
     */
    public static String createSequence(){
        return java.util.UUID.randomUUID().toString();
    }

    /**
     *转换成用B,KB,MB,GB,TB单位来表示的大小
     */
    public static String formatFileLength(long sizes) {
        if(sizes < 0) sizes = 0;
        String str ="";
        if(sizes < 1024) { // 小于1KB
            str += "" + sizes + "B";
        } else if (sizes < 1024*1024) { // 小于1MB
            str += "" + numformat.format(sizes/1024.0) + "K";
        } else if (sizes < 1024*1024*1024) {  // 小于1GB
            str += "" + numformat.format(sizes/(1024*1024.0)) + "M";
        } else if (sizes < 1024*1024*1024*1024L) {  // 小于1TB
            str += "" + numformat.format(sizes/(1024*1024*1024.0)) + "G";
        } else  {  // 大于1TB
            str += "" + numformat.format(sizes/(1024*1024*1024*1024.0)) + "T";
        }
        for(int i=0; i< 8 - str.length(); i++) {
            str = " " + str;
        }
        return str;
    }
    
    /**
     * 把指定byte数组转换成16进制的字符串
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length*2);
        for (byte b : bytes) {
            sb.append(hex[((b >> 4) & 0xF)]).append(hex[((b >> 0) & 0xF)]);
        }
        return sb.toString();
    }
    /**
     * 把指定16进制的字符串转换成byte数组
     */
    public static byte[] hexStringToBytes(String inString) {
        int fromLen = inString.length();
        int toLen = (fromLen + 1) / 2;
        final byte[] b = new byte[toLen];
        for (int i = 0; i < toLen; i++) {
            b[i] = (byte) hexPairToInt(inString.substring(i * 2, (i + 1) * 2));
        }
        return b;
    }

    /**
     * 将数组进行排序然后再组成字符串
     * @param totalStringList
     * @return
     */
    public static String ArrayToSortString(List<String> totalStringList) {
        String str="";

        if(totalStringList!=null && totalStringList.size()>0){
           String[] strs=totalStringList.toArray(new String[totalStringList.size()]);
           Arrays.sort(strs);
           for(String s:strs){
               str+=s;
           }
        }
        return str;
    }
    /**
     * 把指定cid字符串转换成byte数组
     */
    public static byte[] convertStringCid2Bytes(String sCid) {
        byte[] cid = new byte[20];
        for (int i = 0; i < cid.length; i++) {
            cid[i] = (byte) Integer.parseInt(sCid.substring(i * 2, i * 2 + 2), 16);
        }
        return cid;
    }
    
    /**
     * 在指定字符串数组里查找指定字符串，找到则返回索引号，找不到返回-1
     */
    public static int search(String no, String[] noes){
        for(int i=0; i<noes.length; i++){
            if(no.equals(noes[i])) return i;
        }
        return -1;
    }
    
    private static int hexPairToInt(String inString){
        String digits = "0123456789abcdef";
        String s = inString.toLowerCase();
        int n = 0;
        int thisDigit = 0;
        int sLen = s.length();
        if (sLen > 2)  sLen = 2;
        for (int i = 0; i < sLen; i++) {
            thisDigit = digits.indexOf(s.substring(i, i + 1));
            if (thisDigit < 0) throw new NumberFormatException();
            if (i == 0) thisDigit *= 0x10;
            n += thisDigit;
        }
        return n;
    }
    
    public static String read(InputStream in, String charset) throws IOException {
        int pos = -1;
        byte[] buf = new byte[1024*8];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while((pos = in.read(buf)) != -1) {
            out.write(buf, 0, pos);
        }
        return (charset == null) ? new String(out.toByteArray()) : new String(out.toByteArray(), charset);
    }
    
    public static String read(InputStream in) throws IOException {
        return read(in, null);
    }
    
    /**
     * 转换成js代码
     */
    public static final String escapeJs(String unicode) {
        return org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(unicode);
    }

    /**
     * 对字符进行URL编码。客户端使用js的decodeURIComponent进行解码
     * @param str 字符串源码
     * @return URL编码后的字符串
     */
    public static String encodeURL(String str){
                try{
                        return java.net.URLEncoder.encode(str, "utf-8").replaceAll("\\+", "%20");
                }
                catch(Exception ex){
                        return "";
                }
    }

    /**
     * 对url进行解码
     * @param str
     * @return
     */
    public static String decodeURL(String str){
        try{
                return java.net.URLDecoder.decode(str, "utf-8");
        }
        catch(Exception ex){
                return "";
        }
    }
    
    /**
     * 转换成html代码
     */
    public static final String escapeHtml(String unicode) {
        return org.apache.commons.lang.StringEscapeUtils.escapeHtml(unicode);
    }
    
    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().isEmpty();
    }
    /**
     * 判断字符串是否为非空
     */
    public static boolean isNotEmpty(String str){
        return str != null && !str.trim().isEmpty();
    }

    /**
     * 将字符串中可能包含有非法的sql字符进行过滤，例如过滤'。
     * @param str 需要进行过滤的字符串
     * @return 过滤后的安全字符串
     */
    public static final String escapeSql(String str){
        if(str==null){
            return "";
        }
        for(String[] ss:sqlhandles){
            str=str.replaceAll(ss[0], ss[1]);
        }
        return str;
    }

    /**
     * 将数值转换成特定长度的字符串
     * @param value
     * @param length
     * @return
     */
    public static String toLenString(long value,int length){
        String val=value+"";
        if(val.length()>length){
            throw new XLRuntimeException("定义的长度小于数值的长度。");
        }
        if(val.length()<length){
            return zeros.substring(0,length-val.length())+val;
        }
        else{
            return val;
        }
    }

    /**
     * 将字符串中可能包含有非法的sql字符进行过滤，例如过滤'。
     * @param obj 过滤对象
     * @return 过滤后的安全字符串
     */
    public static final String escapeSql(Object obj){
        if(obj==null){
            return "";
        }
        return escapeSql(obj.toString());
    }

    /**
     * 将对象安全转换成int类型，失败时返回0
     * @param o 目标对象
     * @return int数字
     */
    public static int safeToInt(Object o){
        int rs=0;
        try{
            rs=Integer.parseInt(o.toString());
        }
        catch(Exception ex){rs=0;}
        return rs;
    }

        /**
     * 将对象安全转换成short类型
     * @param o 目标对象
     * @return short数字
     */
    public static int safeToShort(Object o){
        short rs=0;
        try{
            rs=Short.parseShort(o.toString());
        }
        catch(Exception ex){rs=0;}
        return rs;
    }

    /**
     * 将对象安全转换成long类型
     * @param o 目标对象
     * @return long数字
     */
    public static long safeToLong(Object o){
        long rs=0;
        try{
            rs=Long.parseLong(o.toString());
        }
        catch(Exception ex){rs=0;}
        return rs;
    }
    
    /**
     * 将对象安全转换成double类型
     * @param o 目标对象
     * @return double数字
     */
    public static double safeToDouble(Object o){
    	double rs=0;
        try{
            rs=Double.parseDouble(o.toString());
        }
        catch(Exception ex){rs=0;}
        return rs;
    }
    
    /**
	 * 得到系统的时间戳
	 */
	public static String getTradeSn(){
		return "" + new java.util.Date().getTime();
	}
	
	 /**
     * 压缩
     * @param str 压缩前的字符串
     * @return 压缩后的字符串
     */
    public static String compress(String str){
        Deflater compresser = new Deflater();
        compresser.setInput(str.getBytes());
        compresser.finish();
        byte[] buf = new byte[4096];//4KB
        int len = compresser.deflate(buf);
        byte[] out = new byte[len];
        for (int i=0;i<len;i++){
            out[i] = buf[i];
        }
        return new BASE64Encoder().encode(out);
    }
    /**
     * 解压
     * @param str 解压前的字符串
     * @return 解压后的字符串
     * @throws Exception
     */
    public static String decompress(String str) throws Exception{
        byte[] decodeBytes = new BASE64Decoder().decodeBuffer(str);
        Inflater decompresser = new Inflater();
        decompresser.setInput(decodeBytes,0,decodeBytes.length);
        byte[] buf = new byte[4096];//4KB
        int len = decompresser.inflate(buf);
        decompresser.end();
        byte[] out = new byte[len];
        for (int i=0;i<len;i++){
            out[i] = buf[i];
        }
        return new String(out);
    }

    /**
     * 尝试将对象转换成double类型，如果失败时也不抛出异常而返回0
     * @param fieldValue
     * @return
     */
    public static double tryParseDouble(Object fieldValue) {
        try{
            double rs=(Double)fieldValue;
            return rs;
        }
        catch(Exception ex){
            try{
                return Double.parseDouble(fieldValue.toString());
            }
            catch(Exception exx){
                return 0;
            }
        }
    }


    public static void main(String[] args){
        System.out.println(escapeSql("ere\\\rrod"));
        //System.out.println("result:'"+StringTools.decodeURL("+")+"'");
    }
    
}


package com.xunlei.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.xunlei.common.event.XLRuntimeException;

/**
 *  提供对IO文件处理方法 
 */
public class FileUtil {
    
    
    public FileUtil(){
    }
    
    /**
     * 拷贝文件
     * @param sfile 源文件
     * @param dfile 目标文件路径
     */
    public static void copyFile(InputStream sfile,String dfile){
        File outputFile = new File(dfile);
        try{
            BufferedInputStream bis = new BufferedInputStream(sfile);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
            int bytes_read;
            byte[] buffer = new byte[4096];
            while ((bytes_read = bis.read(buffer)) != -1){
                bos.write(buffer,0,bytes_read);
            }
            sfile.close();
            bis.close();
            bos.close();
        } catch(Exception e){
            throw new XLRuntimeException("拷贝文件出错：" + e);
        }
    }
    /**
     * 拷贝文件
     * @param sfile 源文件路径
     * @param dfile 目标文件路径
     */
    public static void copyFile(String sfile,String dfile){
        File inputFile = new File(sfile);
        File outputFile = new File(dfile);
        try{
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
            int bytes_read;
            byte[] buffer = new byte[4096];
            while ((bytes_read = bis.read(buffer)) != -1){
                bos.write(buffer,0,bytes_read);
            }
            bis.close();
            bos.flush();
            bos.close();
        } catch(Exception e){
            throw new XLRuntimeException("拷贝文件" + sfile + "出错：" + e);
        }
    }
    /**
     * 拷贝源目录下的文件到目标目录
     * @param spath  源目录路径
     * @param dpath  目标目录路径
     */
    public static void copyFiles(String spath,String dpath){
        String[] files = listFile(spath,null);
        for (int i=0;i<files.length;i++){
            String srcfile = spath + File.separator + files[i];
            String decfile = dpath + File.separator + files[i];
            copyFile(srcfile,decfile);
        }
    }
    
    /**
     * 复制整个文件夹内容
     * @param spath String 原文件路径 如：c:/fqf
     * @param dpath String 复制后路径 如：f:/fqf/ff
     */
    public static void copyFolder(String spath, String dpath) {
        (new File(dpath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
        String[] files = (new File(spath)).list();
        File tf = null;
        String tfspath = null;
        for (int i=0;i<files.length;i++){
            if(spath.endsWith(File.separator)){
                tfspath = spath + files[i];
            } else {
                tfspath = spath + File.separator + files[i];
            }
            tf = new File(tfspath);
            if (tf.isFile()) {
                copyFile(tfspath,dpath + File.separator + tf.getName());
            } else if (tf.isDirectory()){//如果是子文件夹
                copyFolder(spath + File.separator + files[i],dpath + File.separator + files[i]);
            }
        }
    }
    
    
    /**
     * 读取文件
     * @param file 文件路径
     * @return  文件内容，用字符串数列表表示
     */
    public static List<String> readFile(String file) {
        String str = null;
        List<String> result = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
            while((str = br.readLine()) != null){
                result.add(str);
            }
            br.close();
        } catch(Exception e){
            throw new XLRuntimeException("读文件" + file + "出错：" + e);
        }
        return result;
    }

    /**
     * 读取文件
     * @param file 文件路径
     * @return  文件内容，用字符串数表示
     */
    public static String readFile2Str(String file) {
        String str = null;
        StringBuffer sb=new StringBuffer();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
            while((str = br.readLine()) != null){
                sb.append(str+"\n");
            }
            br.close();
        } catch(Exception e){
            throw new XLRuntimeException("读文件" + file + "出错：" + e);
        }
        return sb.toString();
    }
    
    
    /**
     * 写文件
     * @param lines  将要写入的内容
     * @param file   目标文件路径
     */
    public static void writeFile(Collection<String> lines,String file) {
    	newFile(file, "");
        StringBuffer buf = new StringBuffer();
        for (Iterator<String> it = lines.iterator();it.hasNext();){
            String line = it.next().toString();
            buf.append(line).append("\n");
        }
        try{
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file),"GBK");
            osw.write(buf.toString(),0,buf.toString().length());
            osw.flush();
            osw.close();
        } catch(Exception e){
            throw new XLRuntimeException("写文件" + file + "出错：" + e);
        }
    }

    /**
     * 写文件
     * @param filelines  将要写入的内容
     * @param file   目标文件路径
     */   
    public static void writeFile(String filelines,String file) {
        try{
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file),"GBK");
            osw.write(filelines);
            osw.flush();
            osw.close();
        } catch(Exception e){
            throw new XLRuntimeException("写文件" + file + "出错：" + e);
        }
    }

    /**
     * 列出某一目录下符合指定扩展名的所有文件名，目录名后的分隔符可有可无
     * @param path 目录路径
     * @param extnames 以逗号分隔的扩展名列表，如：.txt,.doc,.htm
     */
    public static String[] listFile(String path,String extnames){
        String[] names = null;
        File file = new File(path);
        if ((extnames == null) || (extnames.trim().equals(""))){
            names = file.list();
        } else{
            final String[] exts = extnames.split(",");
            names = file.list(new FilenameFilter(){
                public boolean accept(File dir, String name) {
                    // TODO Auto-generated method stub
                    return inlist(name);
                }
                
                private boolean inlist(String name){
                    if (name.lastIndexOf(".") == -1){
                        return false;
                    }
                    String ext = name.substring(name.lastIndexOf("."));
                    for (int i=0;i<exts.length;i++){
                        if (exts[i].equalsIgnoreCase(ext)){
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
        return names;
    }
    /**
     * 删除指定目录下的所有文件
     * @param path 目录路径
     */
    public static void deleteFiles(String path){
        File p = new File(path);
        if (p.exists() && p.isDirectory()){
            File[] files = p.listFiles();
            for (int i=0;i<files.length;i++){
                if (files[i].isFile()){
                    files[i].delete();
                }
            }
        }
    }
    /**
     * 删除指定目录下的指定文件
     * @param path 目录路径
     * @param files 目标文件
     */
    public static void deleteFiles(String path,String[] files){
        if (!path.endsWith(File.separator)){
            path += File.separator;
        }
        File p = new File(path);
        if (p.exists() && p.isDirectory()){
            for (int i=0;i<files.length;i++){
                File f = new File(path + files[i]);
                f.delete();
            }
        }
    }
    
    /**
     * 删除指定文件
     * @param file 目标文件路径
     */
    public static void deleteFile(String file){
        File f = new File(file);
        f.delete();
    }
    
    /**
     * 新建目录
     * @param folderPath 目录的路径如 c:/fqf    
     */
    public static void newFolder(String folderPath) {
        try {
            File myFilePath = new File(folderPath);
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
            }
        } catch (Exception e) {
            throw new XLRuntimeException("新建目录" + folderPath + "出错：" + e);
        }
    }
    
    /**
     * 新建文件
     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
     * @param fileContent String 文件内容
     */
    public static void newFile(String filePathAndName, String fileContent) {
        try {
            File myFilePath = new File(filePathAndName);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            FileWriter resultFile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(resultFile);
            myFile.println(fileContent);
            myFile.close();
            resultFile.close();
        } catch (Exception e) {
            throw new XLRuntimeException("新建文件" + filePathAndName + "出错：" + e);
        }
    }
    
    /**
     * 新建一个文件，如果存在则覆盖
     * @param filePathAndName
     * @param fileData
     * @return 失败返回null
     * @throws IOException 
     */
    public static void newFile(String filePathAndName, byte[] fileData) throws IOException{
    	File myFilePath = new File(filePathAndName);
        if (!myFilePath.exists()) {
            myFilePath.createNewFile();
        }else{
        	myFilePath.delete();
        }
        FileOutputStream resultFile = new FileOutputStream(myFilePath);
        resultFile.write(fileData);
        resultFile.close();
    }
    
    /**
     *删除文件夹下所有内容，包括此文件夹
     */
    public static void delAll(File f) throws IOException {
        if (!f.exists())//文件夹不存在
            throw new IOException("指定目录不存在:" + f.getName());
        
        boolean rslt = true;//保存中间结果
        if (!(rslt = f.delete())) {//先尝试直接删除
            //若文件夹非空。枚举、递归删除里面内容
            File subs[] = f.listFiles();
            for (int i = 0; i < subs.length; i++) {
                if (subs[i].isDirectory())
                    delAll(subs[i]);//递归删除子文件夹内容
                rslt = subs[i].delete();//删除子文件夹本身
            }
            rslt = f.delete();//删除此文件夹本身
        }
        
        if (!rslt) {
            throw new IOException("无法删除:" + f.getName());
        }
    }
    
    public static void main(String[] args) {
    	deleteFiles("D:/workspace/XLRelease/configfile/package/test/");
	}
}

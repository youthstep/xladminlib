/*
 * Created on Jul 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xunlei.common.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


/**
 * 提供对xml配置文件的操作
 * @author jason
 */
public class XmlUtil {
    
    private static XmlUtil instance;
    private Logger log;
    
    private XmlUtil(){
        log = Logger.getLogger(XmlUtil.class);
    }
    
    public synchronized static XmlUtil getInstance(){
        if (instance == null){
            instance = new XmlUtil();
        }
        return instance;
    }
    /**
     * 从xml文件返回xml内容
     * @param xmlfile xml文件
     */
    public Document getXmlFromFile(String xmlfile){
        try{
            SAXReader saxReader = new SAXReader();
            InputStream is = new FileInputStream(xmlfile);
            Document document = saxReader.read(is);
            is.close();
            return document;
        } catch(Exception e){
            log.error(e);
            return null;
        }
    }
    /**
     * 写xml文件
     * @param xmlfile xml文件
     * @param document 文档对象
     */
    public void writeXmlToFile(String xmlfile,Document document){
        try{
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(xmlfile),format);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch(Exception e){
            log.error(e);
        }
    }
    /** 
     * string2Document 
     * 将字符串转为Document 
     * @param s xml格式的字符串 
     * @return  Document对象
     */ 
    public Document string2Document(String s) 
    { 
       Document doc = null; 
       try 
       { 
            doc = DocumentHelper.parseText(s); 
       }catch(Exception e) 
       {             
            e.printStackTrace();
            log.error(e);
       } 
       return doc; 
    } 

    /**
     * 读取系统配置的元素值
     * @param elementPath 元素路径
     */
    public String getApplicationConfigElementValue(String elementPath){
        String result = null;
        try {
            SAXReader saxReader = new SAXReader();
            InputStream is = getClass().getResourceAsStream("/applicationConfig.xml");
            Document document = saxReader.read(is);
            is.close();
            Iterator it = document.selectNodes(elementPath).iterator();
            if (it.hasNext()){
                Element element = (Element)it.next();
                result = element.getText();
            }
            return result;
        } catch(Exception e){
            log.error(e);
            return null;
        }
    }
    /**
     * 读取系统配置的元素值
     * @param elementPath 元素路径
     */
    public List<String> getApplicationConfigElementValues(String elementPath){
        List<String> result = new ArrayList<String>();
        try {
            SAXReader saxReader = new SAXReader();
            InputStream is = getClass().getResourceAsStream("/applicationConfig.xml");
            Document document = saxReader.read(is);
            is.close();
            for(Iterator it = document.selectNodes(elementPath).iterator();it.hasNext();){
                result.add(((Element)it.next()).getText());
            }
            return result;
        } catch(Exception e){
            log.error(e);
            return result;
        }
    }
    /**
     * 根据xPath路径找xml元素
     * @param xmlfile 要操作的xml文件路径
     * @param elementPath 要寻找的元素xPath路径
     */
    public Element getXmlElementByPath(String xmlfile,String elementPath){
        try {
            Document document = getXmlFromFile(xmlfile);
            Iterator it = document.selectNodes(elementPath).iterator();
            if (it.hasNext()){
                return (Element)it.next();
            }
            return null;
        } catch(Exception e){
            log.error(e);
            return null;
        }
    }
    /**
     * 读取某一xml文件的元素值
     * @param xmlfile 要操作的xml文件路径
     * @param elementPath 元素路径
     */
    public String getApplicationConfigElementValue(String xmlfile,String elementPath){
        String result = null;
        try {
        	Document document = getXmlFromFile(xmlfile);
            Iterator it = document.selectNodes(elementPath).iterator();
            if (it.hasNext()){
                Element element = (Element)it.next();
                result = element.getText();
            }
            return result;
        } catch(Exception e){
            log.error(e);
            return null;
        }
    }
    /**
     * 读取某一xml文件的元素值
     * @param xmlfile 要操作的xml文件路径
     * @param elementPath 元素路径
     */
    public List<String> getApplicationConfigElementValues(String xmlfile,String elementPath){
        List<String> result = new ArrayList<String>();
        try {
        	Document document = getXmlFromFile(xmlfile);
            for(Iterator it = document.selectNodes(elementPath).iterator();it.hasNext();){
                result.add(((Element)it.next()).getText());
            }
            return result;
        } catch(Exception e){
            log.error(e);
            return result;
        }
    }
    
}

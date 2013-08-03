package com.xunlei.libfun.vo;

import org.directwebremoting.annotations.DataTransferObject;

import com.xunlei.common.util.StringTools;



/**
 * 系统数组元素的VO类
 */
@DataTransferObject
public class LibClassD  implements java.io.Serializable {
    
    // Fields
    
    private long seqid = 0L;
    private String classno;
    private String itemno;
    private String classitemid;
    private String itemname;
    private String itemvalue;
    private int itemorder ;
    private String remark ="";
    private String editby;
    private String edittime;
    
    
    // Constructors
    
    /** default constructor */
    public LibClassD() {
    }
    
    /** minimal constructor */
    public LibClassD(String classno, String itemno, String classitemid, String itemname,  String itemvalue,Integer itemorder, String editby, String edittime) {
        this.classno = classno;
        this.itemno = itemno;
        this.classitemid = classitemid;
        this.itemname = itemname;
        this.itemvalue = itemvalue;
        this.itemorder = itemorder;
        this.editby = editby;
        this.edittime = edittime;
    }
    
    /** full constructor */
    public LibClassD(String classno, String itemno, String classitemid, String itemname, String itemvalue, Integer itemorder, String remark, String editby, String edittime) {
        this.classno = classno;
        this.itemno = itemno;
        this.classitemid = classitemid;
        this.itemname = itemname;
        this.itemvalue = itemvalue;
        this.itemorder = itemorder;
        this.remark = remark;
        this.editby = editby;
        this.edittime = edittime;
    }
    
   
    
    @Override
    public boolean equals(Object data){
        if(!(data instanceof LibClassD)) return false;
        LibClassD other = (LibClassD)data;
        return this.seqid == other.seqid;
    }
    
    @Override
    public String toString(){
        return StringTools.listingString(this);
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
    
    public String getItemno() {
        return this.itemno;
    }
    
    public void setItemno(String itemno) {
        this.itemno = itemno;
    }
    
    public String getClassitemid() {
        return this.classitemid;
    }
    
    public void setClassitemid(String classitemid) {
        this.classitemid = classitemid;
    }
    
    public String getItemname() {
        return this.itemname;
    }
    
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
    
    public String getItemvalue() {
        return this.itemvalue;
    }
    
    public void setItemvalue(String itemvalue) {
        this.itemvalue = itemvalue;
    }
    
    public int getItemorder() {
        return this.itemorder;
    }
    
    public void setItemorder(int itemorder) {
        this.itemorder = itemorder;
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
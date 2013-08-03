package com.xunlei.libfun.vo;

import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;

import com.xunlei.common.util.Extendable;
import com.xunlei.common.util.StringTools;



/**
 * 系统菜单的VO类
 */
@DataTransferObject
public class Menus{
	@Extendable
	public static final int INUSE_TRUE = 1;
	@Extendable
	public static final int INUSE_FALSE = 0;
	
    // Fields
    private long seqid;
    private String menuno;
    private String menuname;
    private String pmenuno;
    private String menuurl;
    private String menuhint;
    private String menutarget ="";
    private String funcno = "";
    private long displayorder ;
    private short inuse = 0;
    private short adminvisible=0;
    private short expand=(short)0;//标记是否展开一个菜单下的目录
    private String folderurl;//保存点击菜单节点时跳转到的url
    private String menuid;
    @Extendable
    private String funcname;

	public short getExpand() {
        return expand;
    }

    public void setExpand(short expand) {
        this.expand = expand;
    }

    public String getFolderurl() {
        return folderurl;
    }

    public void setFolderurl(String folderurl) {
        this.folderurl = folderurl;
    }


    // Constructors
    
    
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
    
    public String getMenuno() {
        return this.menuno;
    }
    
    public void setMenuno(String menuno) {
        this.menuno = menuno;
    }
    
    public String getMenuname() {
        return menuname;
    }
    
    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }
    
    public String getPmenuno() {
        return pmenuno;
    }
    
    public void setPmenuno(String pmenuno) {
        this.pmenuno = pmenuno;
    }
    
    public String getMenuurl() {
        return menuurl;
    }
    
    public void setMenuurl(String menuurl) {
        this.menuurl = menuurl;
    }
    
    public String getMenuhint() {
        if(menuhint == null || menuhint.length() < 1) return getMenuname();
        return menuhint;
    }
    
    public void setMenuhint(String menuhint) {
        this.menuhint = menuhint;
    }
    
    public String getMenutarget() {
        return menutarget;
    }
    
    public void setMenutarget(String menutarget) {
        this.menutarget = menutarget;
    }
    
    public String getFuncno() {
        if(funcno == null) return "";
        return funcno;
    }
    
    public void setFuncno(String funcno) {
        this.funcno = funcno;
    }
    
    public long getDisplayorder() {
        return displayorder;
    }
    
    public void setDisplayorder(long displayorder) {
        this.displayorder = displayorder;
    }
    
     public short getInuse() {
        return this.inuse;
    }
    
    public void setInuse(short inuse) {
        this.inuse = inuse;
    }
    
    public boolean isBoolinuse() {
        return inuse > 0;
    }

    public boolean isBoolexpand(){
        return expand>0;
    }
    
    public String getFuncname() {
        return funcname;
    }
    
    public void setFuncname(String funcname) {
        this.funcname = funcname;
    }

    /**
     * @return the adminvisible
     */
    public short getAdminvisible() {
        return adminvisible;
    }

    /**
     * @param adminvisible the adminvisible to set
     */
    public void setAdminvisible(short adminvisible) {
        this.adminvisible = adminvisible;
    }

        public boolean isBooladminvisible() {
        return adminvisible > 0;
    }

		public String getMenuid() {
			return menuid;
		}

		public void setMenuid(String menuid) {
			this.menuid = menuid;
		}    
}
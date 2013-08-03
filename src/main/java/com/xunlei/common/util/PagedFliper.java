/*
 * PagedFliper.java
 *
 * Created on 2007年6月19日, 上午11:23
 *
 * 用于记录页码信息，实现翻页功能
 *
 */

package com.xunlei.common.util;

import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.web.bean.QueryInfo;

/**
 * 用于定义翻页操作，记录显示的排序规则。
 * @author 张金雄
 */
public class PagedFliper extends QueryInfo {
    
    private static final String HIDDEN_PAGESIZE ="__hidden_pagesize_";
    
    private static final String HIDDEN_PAGENO ="__hidden_pageno_";
    
    private static final String HIDDEN_SORTCOLUMN ="__hidden_sortcolumn_";
    
    protected int index = 1;
    
    public PagedFliper() {
        super();
    }
    
    /**
     * 构造函数，设置每页显示的行数
     *@param pageSize 每页显示行数
     */
    public PagedFliper(int pageSize) {
        this.pageSize = pageSize;
    }
    /**
     *@param pageSize 每页显示行数
     *@param pageNo   当前页码
     *@param sortColumn  需要排序的列信息，用于在后台查询时拼凑sql语句，如：username desc, userno asc
     */
    public PagedFliper(int pageSize, int pageNo, String sortColumn) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        if(sortColumn != null) {
            this.sortColumn = sortColumn.trim();
        }
    }
    
    public PagedFliper(QueryInfo pinfo){
    	this(pinfo.getPageSize(),pinfo.getPageNo(),pinfo.getSortColumn());
    }

    /**
     *重置页码，列排序规则信息
     */
    public void reset(){
        this.pageNo = 1;
        this.sortColumn ="";
    }

    /**
     * 根据总的行数，返回当前页所需要的sql子句
     * @param rowcount   总的行数，即数据列表的数据总个数
     */  
    public final String limitsql(int rowcount){
        if(!isValid()) return " ";
        return addLimitToSql(rowcount, getPageSize(), getPageNo());
    }
    
    public String getHiddenPageSizeId(){
        return HIDDEN_PAGESIZE + (getIndex() < 2 ? "" : (""+getIndex()));
    }
    
    public String getHiddenPageNoId(){
        return HIDDEN_PAGENO + (getIndex() < 2 ? "" : (""+getIndex()));
    }
    
    public String getHiddenSortColumnId(){
        return HIDDEN_SORTCOLUMN + (getIndex() < 2 ? "" : (""+getIndex()));
    }

    /**
     * 拼凑需要在页面隐藏的页码变量,如当前页码变量，列排序规则信息变量  
     */  
    public String getFormathtml(){
        String pagesizeid = getHiddenPageSizeId();
        String pagenoid = getHiddenPageNoId();
        String sortcolumnid = getHiddenSortColumnId();
        StringBuilder sb = new StringBuilder(80);
        sb.append("<input type='hidden' id='").append(pagesizeid).append("' name='")
        .append(pagesizeid).append("' value='").append(getPageSize()).append("' />");
        sb.append("<input type='hidden' id='").append(pagenoid).append("' name='")
        .append(pagenoid).append("' value='").append(getPageNo()).append("' />");
        sb.append("<input type='hidden' id='").append(sortcolumnid).append("' name='")
        .append(sortcolumnid).append("' value='").append(getSortColumn()).append("' />");
        return sb.toString();
    }
    
    @Override
    public String toString(){
        return "PagedFliper[pageNo="+this.pageNo + ", pageSize="+this.pageSize
            + ", sortColumn=" + this.getSortColumn() + ", index=" + index + "]";
    }

    /**
     *获取索引号:一般一个PagedFilper对象用于处理某级页面的翻页，顶级页面的索引号默认为1，
     *如果此页面有子级页面，子级页面的PagedFilper对象的索引号就为2，以此类推
     */
    public int getIndex() {
        if(index < 1) return 1;
        return index;
    }
    /**
     *设置索引号
     */
    public void setIndex(int index) {
        if(index > 0) {
            this.index = index;
        }
    }

    public int getSmoothPageNo(){
        int pageCount=getPageCount();
        if(getPageNo()>pageCount){
            return 1;
        }
        return getPageNo();
    }

    public int getSmoothCurrentPageRecordCount(){
        if(this.getSmoothPageNo()<this.getPageCount()){
            return this.getPageSize();
        }
        else{
            return this.recordCount-this.getPageSize()*(this.getPageCount()-1);
        }
    }
   
    /**
     * 获取排序规则
     */
    public String getSortColumn() {
        if("null".equals(sortColumn)) return "";
        return sortColumn;
    }
    /**
     * 设置当没有指定排序规则时的默认排序规则，例如 menuno desc, menuname asc 等等
     */
    public void setSortColumnIfEmpty(String sortColumn) {
        if(this.isEmptySortColumn()) {
            this.setSortColumn(sortColumn);
        }
    }
    /**
     * 设置排序规则，例如 menuno desc, menuname asc 等等
     */
    public void setSortColumn(String sortColumn) {
        if(sortColumn != null && !"null".equals(sortColumn.trim())){
            this.sortColumn = StringTools.escapeSql(sortColumn.trim()).replaceAll("-", "");
        }
    }
    /**
     * 判断排序规则是否为空
     */
    public boolean isEmptySortColumn(){
        return this.getSortColumn() == null || this.getSortColumn().length() == 0;
    }
    
    /**
     * 判断排序规则是否为非空
     */
    public boolean isNotEmptySortColumn(){
        return this.getSortColumn() != null && this.getSortColumn().length() != 0;
    }
    
    private boolean isValid(){
        return getPageSize() > 0 && getPageNo() > 0;
    }
    
    /**
     * 计算翻页的起始行索引号
     * @param rowcount   总的行数，即数据列表的数据总个数
     * @param pagesize   每页显示的行数
     * @param pageno     当前页码
     * @return
     */   
    private static int computeStartRowNo(int rowcount,int pagesize,int pageno){
        if ((rowcount < 0) || (pagesize < 1) || (pageno < 1)){
            throw new XLRuntimeException("用于计算翻页起始行的参数不正确!");
        }
        if (rowcount == 0) return 0;
        int startrowno = (pageno - 1) * pagesize;
        if (startrowno >= rowcount){
            if (rowcount % pagesize == 0){
                startrowno = rowcount - pagesize;
            } else{
                startrowno = rowcount / pagesize * pagesize;
            }
        }
        return startrowno;
    }
    
    private static String addLimitToSql(int rowcount,int pagesize,int pageno){
        int startrowno = computeStartRowNo(rowcount,pagesize,pageno);
        return " limit " + startrowno + "," + pagesize;
    }

    /**
     * 重新计算参数。当设置了所有记录总数时，比如总记录数减少了，这个时候pageno可能超过了总页数，所以需要重新计算
     * 如果重新计算了则返回TRUE
     */
    public boolean reCountParameter(){
        int pc=getPageCount();
        if(this.pageNo>pc){
            pageNo=pc;
            return true;
        }
        return false;
    }
    
    /**
     * 获得当前页的记录起点，从1开始
     * @return
     */
    public int getCurrentPageRecordStartIndex(){
        if(getRecordCount()==0){
            return 0;
        }
        else{
            return (getSmoothPageNo()-1)*pageSize + 1;
        }
    }

    /**
     * 获得当前页的记录终点，从1开始
     * @return
     */
    public int getCurrentPageRecordEndIndex(){
        int startIndex= getCurrentPageRecordStartIndex();
        if(startIndex==0){
            return 0;
        }
        return startIndex-1+getCurrentPageRecordCount();
    }

    public static void main(String[] args){
        PagedFliper fliper=new PagedFliper(2, 1, "");
        fliper.setRecordCount(100);
        System.out.println(fliper.getCurrentPageRecordStartIndex()+".."+fliper.getCurrentPageRecordEndIndex());
    }


    /**
     * 设置记录数后计算总页数
     */
    public int getPageCount(){
        if(recordCount==0){
            return 0;
        }
        return recordCount%pageSize==0 ? recordCount/pageSize :recordCount/pageSize+1;
    }

    public int getCurrentPageRecordCount(){
        if(this.getPageNo()<this.getPageCount()){
            return this.getPageSize();
        }
        else{
            return this.recordCount-this.getPageSize()*(this.getPageCount()-1);
        }
    }
    
    /**
     * 获得用于设置特定某页的脚本。例如document.getElementById('__hidden_pageNo_').value='4'
     */
    public String getSetPageNoScript(String pageNo){
        StringBuilder sb=new StringBuilder();
        sb.append("document.getElementById('").append(this.getHiddenPageNoId()).append("').value=").append(pageNo).append(";if(typeof(doPager)=='function')doPager();");
        return sb.toString();
    }

    public String getSetPageSizeStript(String pageSize){
        StringBuilder sb=new StringBuilder();
        sb.append("document.getElementById('").append(this.getHiddenPageSizeId()).append("').value=").append(pageSize).append(";");
        sb.append("document.getElementById('").append(this.getHiddenPageNoId()).append("').value=1;");
        sb.append("if(typeof(doPager)=='function')doPager();");
        return sb.toString();
    }
}



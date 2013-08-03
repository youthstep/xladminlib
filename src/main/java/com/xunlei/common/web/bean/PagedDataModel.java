/*
 * PagedDataModel.java
 *
 * Created on 2007年8月24日, 下午1:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.web.bean;

import com.xunlei.common.plugin.PagedDataModelPluginable;
import java.util.Collection;
import com.xunlei.common.util.*;
import com.xunlei.libfun.vo.LibConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 此类用于显示某个页面的查询数据，可支持翻页。
 * 支持最多4个翻页插件，只需实现PagedDataModelPluginable接口
 * @author 张金雄 
 */
public class PagedDataModel implements java.io.Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 210476721351113518L;

	private final int index;
    
    private final DataAccessReturn<?> sheet;
    
    private final PagedFliper fliper;
    
    private static final PagedDataModelPluginable plugin;

    private static final int[] initPagesizeArray={20,50,100};

    private static int[] pagesizeArray=null;
    
    static {
        PagedDataModelPluginable one = null;
        try {            
            ServiceLoader<PagedDataModelPluginable> loader = ServiceLoader.load(PagedDataModelPluginable.class);
            Iterator<PagedDataModelPluginable> it = loader.iterator();
            if(it.hasNext()) one = it.next();
        } catch (Throwable ex){
            ex.printStackTrace();
        }
        initPageSizeArray();
        plugin = one;
    }
    public PagedDataModel() {
        super();
        this.sheet = DataAccessReturn.EMPTY;
        this.fliper = null;
        this.index = 0;
    } 
    /**
     *@param sheet 将要显示的数据列表
     *@param fliper 页码信息，用于实现翻页功能
     *@param index  页面中页码信息隐藏变量的编号
     */
    public PagedDataModel(DataAccessReturn<?> sheet, PagedFliper fliper, int index) {
        this.sheet = sheet;
        this.fliper = fliper;
        this.index = index;
    } 
    /**
     *@return 页面显示的数据列表
     */
    public Collection<?> getDatas(){
        return getSheet().getDatas();
    }
    
    /**
     * @see com.xunlei.common.util.PagedFliper#getFormathtml()
     */
    @Deprecated
    public String getHiddenhtml() {
        return getFliper().getFormathtml();
    }

    /**
     * 拼凑需要在页面隐藏的页码变量,js脚本，用于实现翻页功能。 
     */  
    public String getFooterhtml() {
    	if(getSheet() == null || getFliper() == null){
    		return "";
    	}
        final int rowcount = getSheet().getRowcount();
        getFliper().setRecordCount(rowcount);
        final int pageSize = getFliper().getPageSize();
        int pageNo = getFliper().getPageNo();
        final int pagecount =(rowcount + pageSize -1)/pageSize;
        ////当前记录开始的行号
        StringBuilder sb =new StringBuilder(300);
        //sb.append("\n<table id=\"_foot_pagination\"  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
        //sb.append("\n    <tr>\n  <td id=\"_foot_stauts_label\" align=\"left\" class=\"fnormal\"></td>      <td align=\"right\" class=\"fnormal\">");
        sb.append("<span class='pagerDisp'>");
        if(pagecount>0){
            sb.append("<script type='text/javascript'>function setPageSize(pageno,pagesize,index){var pagesizehdid='__hidden_pagesize_';if(index>1){pagesizehdid+=index;}var o=document.getElementById(pagesizehdid);o.value=pagesize;skipto(1,index);}</script>");
            sb.append("&nbsp;&nbsp;每页显示：");
            for(int i=0;i<pagesizeArray.length;i++){
                int a=pagesizeArray[i];
                if(pageSize==a){
                    sb.append(a);
                }
                else{
                    sb.append("<a href='javascript:void(0)' onclick=\"setPageSize(").append(pageNo).append(",").append(a).append(",").append(this.getIndex()).append(")\">").append(a).append("</a>");
                }
                if(pagesizeArray.length-1>i){
                    sb.append("，");
                }
            }
        }
        sb.append("</span>");

        sb.append("<span class='pagerNum'>");
        sb.append(escapeHtml("共")).append(pagecount).append(escapeHtml("页")).append("&nbsp;[");
        sb.append(fliper.getCurrentPageRecordStartIndex()).append("..").append(fliper.getCurrentPageRecordEndIndex()).append("/").append(rowcount);
        sb.append("]&nbsp;&nbsp;[");
        if(pageNo > 1) {
            sb.append("<a class='listtree' href=\"javascript:skipto('").append(pageNo-1).append("',"+this.getIndex()+");\">");
        }
        sb.append(escapeHtml("上一页")).append((pageNo > 1) ? "</a>" : "").append("]").append(escapeHtml("第"));
        if(pagecount <= 50) {
            sb.append("<select onChange = \"skipto(this.value,"+this.getIndex()+");\">");
            for(int i=1; i<= pagecount; i++) {
                sb.append("<option value=\"").append(i).append("\" ").append((pageNo == i) ? "selected" : "").append(">").append(i).append("</option>");
            }
            sb.append("</select>");
        } else {
            sb.append("&nbsp;<input title='"+escapeHtml("回车跳到指定页")+"' value='"+pageNo+"' onkeypress='pressskippage(this, event, "+pagecount+", "+this.getIndex()+");' style='text-align: center;height: 20px;font-size: 11px;' size='3'>&nbsp;");
        }
        sb.append(escapeHtml("页")).append("&nbsp;[");
        if(pageNo < pagecount) {
            sb.append("<a class='listtree' href=\"javascript:skipto('").append(pageNo+1).append("',"+this.getIndex()+");\">");
        }
        sb.append(escapeHtml("下一页")).append((pageNo < pagecount) ? "</a>" : "").append("]&nbsp;&nbsp;</span>");

        //sb.append("&nbsp;&nbsp;\n        </td>\n    </tr>\n</table>\n");
        return sb.toString();
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml2方法
     */  
    public String getFooterhtml2() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml2(this);
        return (html == null ? getFooterhtml() : html);
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml3方法
     */ 
    public String getFooterhtml3() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml3(this);
        return (html == null ? getFooterhtml() : html);
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml4方法
     */ 
    public String getFooterhtml4() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml4(this);
        return (html == null ? getFooterhtml() : html);
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml5方法
     */ 
    public String getFooterhtml5() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml5(this);
        return (html == null ? getFooterhtml() : html);
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml6方法
     */ 
    public String getFooterhtml6() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml6(this);
        return (html == null ? getFooterhtml() : html);
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml7方法
     */ 
    public String getFooterhtml7() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml7(this);
        return (html == null ? getFooterhtml() : html);
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml8方法
     */ 
    public String getFooterhtml8() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml8(this);
        return (html == null ? getFooterhtml() : html);
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml9方法
     */ 
    public String getFooterhtml9() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml9(this);
        return (html == null ? getFooterhtml() : html);
    }
    /**
     *@see #getFooterhtml()
     *调用插件实现类的getFooterhtml10方法
     */ 
    public String getFooterhtml10() {
        if(plugin == null) return getFooterhtml();
        String html = plugin.getFooterhtml10(this);
        return (html == null ? getFooterhtml() : html);
    }
    
    private static String escapeHtml(String str){
        return StringTools.escapeHtml(str);
    }
    /**
     *@return 页面中页码信息隐藏变量的编号
     */ 
    public int getIndex() {
        return index;
    }
    
    /**
     *@return 用于记录每次请求后台需要在页面上显示的数据列表
     */
    public DataAccessReturn<?> getSheet() {
        return sheet;
    }
    /**
     *@return 页码信息,用于处理翻页
     */
    public PagedFliper getFliper() {
        return fliper;
    }

    public static void initPageSizeArray() {
        int defalutPageSize = Integer.parseInt(LibConfig.getValue(Constants.PAGESIZE));
        List<Integer> pages = new ArrayList<Integer>();
        boolean hasAdd = false;
        for (int i : initPagesizeArray) {
            if (i == defalutPageSize) {
                hasAdd = true;
            }
            pages.add(i);
        }
        if (!hasAdd) {
            pages.add(defalutPageSize);
            java.util.Collections.sort(pages);
        }

        pagesizeArray = new int[pages.size()];
        for (int i = 0; i < pages.size(); i++) {
            pagesizeArray[i] = pages.get(i);
        }
    }
} 


/*
 * PagedDataModelPluginable.java
 *
 * Created on 2007年12月17日, 下午2:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.xunlei.common.plugin;

import com.xunlei.common.web.bean.PagedDataModel;

/**
 * 翻页插件
 * @author 张金雄 
 */
public interface PagedDataModelPluginable extends Pluginable{
    public String getFooterhtml2(PagedDataModel data);
    public String getFooterhtml3(PagedDataModel data);
    public String getFooterhtml4(PagedDataModel data);
    public String getFooterhtml5(PagedDataModel data);
    public String getFooterhtml6(PagedDataModel data);
    public String getFooterhtml7(PagedDataModel data);
    public String getFooterhtml8(PagedDataModel data);
    public String getFooterhtml9(PagedDataModel data);
    public String getFooterhtml10(PagedDataModel data);
}

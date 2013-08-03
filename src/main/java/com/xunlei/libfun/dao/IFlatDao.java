package com.xunlei.libfun.dao;

import java.util.List;

import com.xunlei.libfun.vo.Flatnofieldvalue;
import com.xunlei.libfun.vo.UserInfo;

/**
 * 公告类型操作类的接口
* @author 李亚俊
* 2008-12-10 16:32:23
*
*/
public interface IFlatDao{
    /**
     * 从业务表中获得平台编号和平台名称的列表
     * @param flatid 平台编号
     * @param flatname 平台名称
     * @param flatindex 拼音简码
     * @return 符合条件的平台编号和平台名称的列表
     */
    public List<Flatnofieldvalue> getFlatnoAndName(String flattable,String flatid,String flatname,String flatindex);
    /**
     * 从业务表中获得平台编号和平台名称的列表
     * @param flatid 平台编号
     * @param flatname 平台名称
     * @param flatindex 拼音简码
     * @param recnofield 数据角色字段名
     * @param roleno 用户数据角色集合
     * @return 符合条件的平台编号和平台名称的列表
     */
    public List<Flatnofieldvalue> getFlatnoAndName(String flattable,String flatid,String flatname,String flatindex,String recnofield,String[] roleno);
    
    /**
     * 从业务表中获得平台编号和平台名称的列表
     * @param UserInfo 系统用户的基本信息，权限信息，角色信息
     * @return 符合条件的平台编号和平台名称的列表
     */
    public List<Flatnofieldvalue> getFlatnoAndName(UserInfo usr);
}
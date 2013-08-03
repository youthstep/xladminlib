/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xunlei.libfun.dao;

import com.xunlei.common.dao.ICommonDao;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.libfun.vo.LibClassD;
import com.xunlei.libfun.vo.UserInfo;

import java.util.List;

/**
 *
 * @author IceRao
 */
public interface ILibClassDDao  extends ICommonDao{

    int countLibclassd(LibClassD libclass);

    /**
     * 获取一个libconfigd
     */
    LibClassD findLibClassD(final LibClassD data);
 
    /**
	 * 获取一个LibClassD
	 */
    LibClassD getLibClassD(LibClassD libclassd);

    /**
     * 根据classno获得所有符合条件的LibClassD结果集。
	 * @param classno 系统数组编号
	 * @return LibClassD对象的List
     * @see com.xunlei.common.facade.FacadeCommonImpl#getLibClassDByClassNo(String)
     */
    List<LibClassD> getLibClassDByClassNo(String classno);

    /**
     * 插入一个LibClassD对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibClassD(LibClassD)
     */
    void insertLibClassD(LibClassD data);

    /**
     * 获得所有符合查询条件的LibClassD对象
	 * @param ld 查询的条件
	 * @param fliper 分页操作对象，可定义查询所需的排序字段和查询记录数
	 * @return Sheet对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#queryLibClassds(LibClassD,PagedFliper)
     */
    DataAccessReturn<LibClassD> queryLibClassds(LibClassD ld, PagedFliper fliper);

    /**
	 * 获得某一用户的LibClassD对象列表
	 * @param userInfo 某一用户信息
	 * @return LibClassD对象的List
	 */
    List<LibClassD> queryLibClassdsByUserlogno(UserInfo userInfo);

    /**
     * 删除一个LibClassD对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassD(LibClassD)
     */
    void removeLibClassD(LibClassD data);

    /**
     * 删除一个主键为seqid的LibClassD对象
	 * @param seqid 指定对象的主键
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassD(long)
     */
    void removeLibClassD(long seqid);

    /**
     * 更新一个LibClassD对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibClassD(LibClassD)
     */
    void updateLibClassD(LibClassD data);

}

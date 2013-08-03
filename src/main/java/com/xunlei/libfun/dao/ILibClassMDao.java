/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xunlei.libfun.dao;

import com.xunlei.common.dao.ICommonDao;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.libfun.vo.LibClassM;

import java.util.List;

/**
 *
 * @author IceRao
 */
public interface ILibClassMDao  extends ICommonDao{

	/**
	 * 获得符合查询条件的LibClassM对象个数
	 * @param data 查询的条件
	 * @return 结果个数
	 */
    int countLibclassm(LibClassM data);

    /**
	 * 获得符合条件的首个LibClassM对象
	 */
    LibClassM getALibClassM(LibClassM libclassm);

    /**
     * 获得所有的LibClassM结果集
     * @see com.xunlei.common.facade.FacadeCommonImpl#getAllLibClassM()
     */
    List<LibClassM> getAllLibClassM();

    /**
     * 获得所有符合查询条件的LibClassM对象
	 * @param libclassm 查询的条件
	 * @return LibClassM对象的List
     */
    List<LibClassM> getLibClassM(LibClassM libclassm);

    /**
     * 插入一个LibClassM对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibClassM(LibClassM)
     */
    void insertLibClassM(LibClassM data);

    /**
     * 获得所有符合查询条件的LibClassM对象
	 * @param config 查询的条件
	 * @param fliper 分页操作对象，可定义查询所需的排序字段和查询记录数
	 * @return Sheet对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#queryLibclassms(LibClassM,PagedFliper)
     */
    DataAccessReturn<LibClassM> queryLibclassms(LibClassM config, PagedFliper fliper);

    /**
     * 删除一个LibClassM对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassM(LibClassM)
     */
    void removeLibClassM(LibClassM data);

    /**
     * 删除一个指定seqid的LibClassM对象
     * @param seqid 指定对象的主键
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibClassM(long)
     */
    void removeLibClassM(long seqid);

    /**
     * 更新一个LibClassM对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibClassM(LibClassM)
     */
    void updateLibClassM(LibClassM data);

}

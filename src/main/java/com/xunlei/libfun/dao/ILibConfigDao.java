/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xunlei.libfun.dao;

import com.xunlei.common.dao.ICommonDao;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;
import com.xunlei.libfun.vo.LibConfig;

import java.util.List;

/**
 *
 * @author IceRao
 */
public interface ILibConfigDao  extends ICommonDao{

	/**
	 * 获得符合条件的LibConfig结果集
	 */
    public List<LibConfig> getLibConfig(LibConfig libconfig);

    /**
	 * 获得符合查询条件的LibConfig对象个数
	 * @param config 查询的条件
	 * @return 结果个数
	 */
    int countLibconfigs(LibConfig config);

    /**
     * 获得所有的LibConfig结果集
     * @see com.xunlei.common.facade.FacadeCommonImpl#getAllLibConfig()
     */
    List<LibConfig> getAllLibConfig();

    /**
	 * 获得符合条件的首个LibConfig对象
	 */
    LibConfig getALibConfig(LibConfig libconfig);

    /**
     * 插入一个LibConfig对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#insertLibConfig(LibConfig)
     */
    void insertLibConfig(LibConfig data);

    /**
     * 获得所有符合查询条件的LibConfig对象
	 * @param config 查询的条件
	 * @param fliper 分页操作对象，可定义查询所需的排序字段和查询记录数
	 * @return Sheet对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#queryLibconfigs(LibConfig,PagedFliper)
     */
    DataAccessReturn<LibConfig> queryLibconfigs(LibConfig config, PagedFliper fliper);

    /**
     * 删除一个LibConfig对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibConfig(LibConfig)
     */
    void removeLibConfig(LibConfig data);

    /**
     * 删除一个主键为seqid的LibConfig对象
	 * @param seqid 指定对象的主键
     * @see com.xunlei.common.facade.FacadeCommonImpl#removeLibConfig(long)
     */
    void removeLibConfig(long seqid);

    /**
     * 更新一个LibConfig对象
     * @see com.xunlei.common.facade.FacadeCommonImpl#updateLibConfig(LibConfig)
     */
    void updateLibConfig(LibConfig data);

}

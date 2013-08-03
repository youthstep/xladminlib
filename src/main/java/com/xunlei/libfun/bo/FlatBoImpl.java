package com.xunlei.libfun.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xunlei.common.bo.DataAccessBo;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.annotations.RemoteMethod;
import com.xunlei.libfun.dao.IFlatDao;
import com.xunlei.libfun.vo.Flatnofieldvalue;

/**
 * @author liheng
 * @since 2011-10-27 下午2:03:10
 */
@Service("flatService")
@RemoteProxy(name="flatService")
public class FlatBoImpl extends DataAccessBo<Flatnofieldvalue> implements IFlatBo{
	@Autowired
	public IFlatDao flatDao;
	
	@RemoteMethod
	public List<Flatnofieldvalue> getQueryFlatnoList() {
		return flatDao.getFlatnoAndName(currentUserInfo());
	}
}

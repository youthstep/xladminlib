package com.xunlei.libfun.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.xunlei.common.dao.JdbcBaseDao;
import com.xunlei.common.util.ApplicationConfigUtil;
import com.xunlei.common.util.StringTools;
import com.xunlei.libfun.vo.Flatnofieldvalue;
import com.xunlei.libfun.vo.LibClassD;
import com.xunlei.libfun.vo.LibClassM;
import com.xunlei.libfun.vo.UserInfo;



/**
 * 公告类型操作类
* @author 李亚俊
* 2008-12-10 16:32:23
*
*/
@Component
public class FlatDaoImpl extends JdbcBaseDao implements IFlatDao {
    /**
     * 从业务表中获得平台编号和平台名称的列表
     * @param flatid 平台编号
     * @param flatname 平台名称
     * @param flatindex 拼音简码
     * @return 符合条件的平台编号和平台名称的列表
     */
    public List<Flatnofieldvalue> getFlatnoAndName(String flattable,String flatid,String flatname,String flatindex){
    	final List<Flatnofieldvalue> datas = new ArrayList<Flatnofieldvalue>();
    	String sql = "";
    	if(StringTools.isNotEmpty(flatindex))
    		sql = "select distinct "+flatid+" as flatno, "+flatname+" as flatname, "+flatindex+" as chooseflag from "+flattable
    			+" order by "+flatindex+" asc, "+flatname+" asc ";
    	else
    		sql = "select distinct "+flatid+" as flatno, "+flatname+" as flatname from "+flattable+" order by "+flatname+" asc ";
		getJdbcTemplate().query(sql,new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException {
				Flatnofieldvalue gmdata = new Flatnofieldvalue();
				gmdata.setFlatno(rs.getString("flatno"));
				String chooseflag = rs.getString("chooseflag");
				if(chooseflag == null)
					chooseflag = "";
				gmdata.setFlatname(chooseflag + " - " + rs.getString("flatname"));
				datas.add(gmdata);
			}
		});
		return datas;
    }
    /**
     * 从业务表中获得平台编号和平台名称的列表
     * @param flatid 平台编号
     * @param flatname 平台名称
     * @param flatindex 拼音简码
     * @param recnofield 数据角色字段名
     * @param roleno 用户数据角色集合
     * @return 符合条件的平台编号和平台名称的列表
     */
    public List<Flatnofieldvalue> getFlatnoAndName(String flattable,String flatid,String flatname,String flatindex,String recnofield,String[] roleno){
    	if( roleno != null && roleno.length >0){
    		String sql = "";
        	final List<Flatnofieldvalue> datas = new ArrayList<Flatnofieldvalue>();
	        String roles = "";
	        for( int i = 0 ; i < roleno.length; i++){
	        	roles +="'"+ roleno[i]+"',";
	        }
	        roles = roles.substring(0,roles.length()-1);	
	        if(StringTools.isNotEmpty(flatindex))
	        	sql = "select distinct "+flatid+" as flatno, "+flatname+" as flatname, "+flatindex+" as chooseflag from "+flattable
	        		+" where "+recnofield+" in (" + roles + ") order by "+flatindex+" asc, "+flatname+" asc ";	
	        else
	        	sql = "select distinct "+flatid+" as flatno, "+flatname+" as flatname from "+flattable+" where "+recnofield+" in (" + roles + ") order by "+flatname+" asc ";
			getJdbcTemplate().query(sql,new RowCallbackHandler(){
				public void processRow(ResultSet rs) throws SQLException {
					Flatnofieldvalue gmdata = new Flatnofieldvalue();
					gmdata.setFlatno(rs.getString("flatno"));
					String chooseflag = rs.getString("chooseflag");
					if(chooseflag == null)
						chooseflag = "";
					gmdata.setFlatname(chooseflag + " - " + rs.getString("flatname"));
					datas.add(gmdata);
				}
			});
			return datas;
		}else{
			return new ArrayList<Flatnofieldvalue>();
		}	
    }
	@Override
	public List<Flatnofieldvalue> getFlatnoAndName(UserInfo usr) {
		String flatclassno =ApplicationConfigUtil.getFlatclassno();
		String flattable =ApplicationConfigUtil.getFlattable();
		//公告类型不区分平台，配置文件中相应的参数为：
		//flatclassno= 
		//flattable =
		if(StringTools.isEmpty(flatclassno) && StringTools.isEmpty(flattable))
		{
			Flatnofieldvalue f = new Flatnofieldvalue();
			f.setFlatno("-1");
			f.setFlatname("不区分平台");
			List<Flatnofieldvalue> l = new ArrayList<Flatnofieldvalue>();
			l.add(f);
			return l;
		}
		//公告类型区分平台，配置文件中相应的参数为：
		//flatclassno= 
		//flattable = games
		else if(StringTools.isEmpty(flatclassno) && StringTools.isNotEmpty(flattable))
		{
			String flatid =ApplicationConfigUtil.getFlatid();
			String flatname =ApplicationConfigUtil.getFlatname();
			String flatindex =ApplicationConfigUtil.getFlatindex();
			String recnofield =ApplicationConfigUtil.getRecnofield();
			List<Flatnofieldvalue> list = new ArrayList<Flatnofieldvalue>();
			if(StringTools.isNotEmpty(flatid) && StringTools.isNotEmpty(flatname)){
				//boolean datanotcontrol = false;
				//datanotcontrol = commfacade.isNotDataControlByRoles(usr.getSysRolenos());
//				if(StringTools.isEmpty(recnofield) || usr.isSuperman())
//				{
					list = this.getFlatnoAndName(flattable,flatid,flatname,flatindex);
//				}
//				else
//				{
//					list = this.getFlatnoAndName(flattable,flatid,flatname,flatindex,recnofield,usr.getRecRolenos());
//				}
			}
			return list;
		}
		//公告类型区分平台，配置文件中相应的参数为：
		//flatclassno= FlatNo
		//flattable =
		//两个都有值时以 flatclassno 为准
		else
		{
			List<LibClassD> listd = LibClassM.getLibClassDList(flatclassno);
			List<Flatnofieldvalue> l = new ArrayList<Flatnofieldvalue>();
			if (listd.size() > 0) {
				for (int i = 0; i < listd.size(); i++) {
					Flatnofieldvalue item = new Flatnofieldvalue();
					item.setFlatno(listd.get(i).getItemno());
					item.setFlatname(listd.get(i).getItemname());
					l.add(item);
				}
			}
			return l;
		}
	}
}

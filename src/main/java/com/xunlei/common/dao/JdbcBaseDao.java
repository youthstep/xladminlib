package com.xunlei.common.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xunlei.common.dao.annotation.PrimaryKey;
import com.xunlei.common.event.XLRuntimeException;
import com.xunlei.common.util.AssemInterceptor;
import com.xunlei.common.util.DatetimeUtil;
import com.xunlei.common.util.Extendable;
import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.util.StringTools;
import com.xunlei.common.vo.StringInfo;
import com.xunlei.common.web.bean.DataAccessReturn;

/**
 * Dao的基类，提供基本，公用的数据库访问方法。
 * 
 * @author jason
 */
@SuppressWarnings("unchecked")
public abstract class JdbcBaseDao extends JdbcDaoSupport {
	
    protected static final Log logger = LogFactory.getLog("auth");

    private ITableNameProvider iTableNameProvider=null;

    protected ITableNameProvider getTableNameProvider(){
        if(iTableNameProvider==null){
            iTableNameProvider=new DefaultTableNameProvider();
        }
        return iTableNameProvider;
    }

    protected void setTableNameProvider(ITableNameProvider provider){
        this.iTableNameProvider=provider;
    }

	public JdbcBaseDao() {
		super();
        logger.debug(this.getClass().getName()+"初始化");
	}

	/**
	 * 根据某一sql语句返回记录列表
	 */
	public <T> List<T> queryToList(String sql) {
		final List<T> datas = new ArrayList<T>();
		getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				datas.add((T) rs.getObject(1));
			}
		});
		return datas;
	}
        

        /**
         * 通过传入vo类型和用于查询的sql来获得分页的对象列表
         * @param <T>
         * @param clazz vo类型
         * @param sql 用于查询的sql
         * @param sqlcount 用于统计总记录数的sql
         * @param fliper 分页对象
         * @param exargs 附加vo列
         * @return
         */
        protected <T> DataAccessReturn<T> queryToSheet(Class clazz,String sql,String sqlcount,PagedFliper fliper,String... exargs){
            List<T> list=null;
            if(fliper==null){
                list=query(clazz,sql,exargs);
                return new DataAccessReturn<T>(list.size(), list);
            }

            int rowCount = getSingleInt(sqlcount);
            if (fliper.isNotEmptySortColumn()) {
                sql+=" order by "+fliper.getSortColumn();
            }
            sql+=fliper.limitsql(rowCount);
            if (rowCount == 0) {
                list = Collections.EMPTY_LIST;
            } else {
                list = query(clazz, sql,exargs);
            }
            return new DataAccessReturn<T>(rowCount, list);
        }

	/**
	 * 根据某一sql语句返回记录Map对象
	 */
	public <K, V> Map<K, V> queryToMap(String sql) {
		return queryToMap(sql, null, null);
	}

	public <K, V> Map<K, V> queryToMap(String sql, K key, V value) {
		final Map<K, V> datas = new LinkedHashMap<K, V>();
		if (key != null && value != null)
			datas.put(key, value);
		getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				datas.put((K) rs.getObject(1), (V) rs.getObject(2));
			}
		});
		return datas;
	}

	/**
	 * 执行更新的sql语句，返回更新成功的记录数
	 */
	public int executeUpdate(String sql) {
        logger.debug("update sql:"+sql);
		return getJdbcTemplate().update(sql);
	}

	/**
	 * 执行批量的sql语句
	 */
	public int[] batchUpdate(String... sqls) {
		return getJdbcTemplate().batchUpdate(sqls);
	}

	/**
	 * 执行某一sql语句
	 */
	public void execute(String sql) {
		logger.debug("execute sql:"+sql);
		getJdbcTemplate().execute(sql);
	}

	/**
	 * 执行某一sql语句，返回一int数据
	 */
	public int getSingleInt(String sql) {
		try {
			logger.debug("get int sql:"+sql);
			return getJdbcTemplate().queryForInt(sql);
		} catch (DataAccessException e) {
			// don't care the exception
			return 0;
		}
	}

	/**
	 * 执行某一sql语句，返回一long数据
	 */
	public long getSingleLong(String sql) {
		try {
			logger.debug("get long sql:"+sql);
			return getJdbcTemplate().queryForLong(sql);
		} catch (DataAccessException e) {
			// don't care the exception
			return 0L;
		}
	}

	/**
	 * 执行某一sql语句，返回一double数据
	 */
	public double getSingleDouble(String sql) {
		try {
			logger.debug("get double sql:"+sql);
			Double d = (Double) getJdbcTemplate().queryForObject(sql,
					Double.class);
			if (null != d) {
				return d.doubleValue();
			} else {
				return 0D;
			}
		} catch (DataAccessException e) {
			// don't care the exception
			return 0D;
		}
	}

	/**
	 * 执行某一sql语句，返回一字符串数据
	 */
	public String getSingleString(String sql) {
		try {
			logger.debug("get String sql:"+sql);
			String data = (String) getJdbcTemplate().queryForObject(sql,
					String.class);
			if (isEmpty(data)) {
				data = "";
			}
			return data;
		} catch (DataAccessException e) {
			// don't care the exception
			return "";
		}
	}

	/**
	 * 插入某一条记录
	 * 
	 * @param data
	 *            VO数据类
	 */
	public void insertObject(Object data) {		
        this.saveObject(data);
	}

	/**
	 * 批量删除某张表指定的seqid记录
	 * 
	 * @param table
	 *            表名
	 * @param seqids
	 *            指定的seqid
	 */
	public void deleteObject(String table, long... seqids) {
		String sql = "delete from " + table + " where seqid in ("
				+ toString(seqids) + ") ";
		getJdbcTemplate().execute(sql);
	}

	/**
	 * 返回某张表指定的列，目前最大id值,通常用于需要程序自动生成id，并回收利用已删除记录的id
	 * 
	 * @param table
	 *            表名
	 * @param idname
	 *            字段名称（指定的列）
	 * @param len
	 *            用于表示id的字符串最大长度
	 */
	public String createNextId(String table, String idname, int len) {
		int d = 1;
		for (int i = 0; i < len - 1; i++)
			d *= 10;
		String sql = "select (ifnull(max(" + idname + "), '" + d
				+ "')+1) as 'nextid' from " + table + " ";
		String str = String.valueOf(getJdbcTemplate().queryForInt(sql));
		String result = "";
		for (int i = 0; i < len - str.length(); i++) {
			result += "0";
		}
		return result + str;
	}

	/**
	 * 返回符合某一sql语句的第一条记录
	 * 
	 * @param clazz
	 *            指定的VO类
	 * @param sql
	 *            sql语句
	 * @param exargs
	 */
	public <T> T queryOne(final Class<T> clazz, final String sql,
			final String... exargs) {
		return queryOne(clazz, sql, null, exargs);
	}

	/**
	 * 返回符合某一sql语句的第一条记录
	 * 
	 * @param clazz
	 *            指定的VO类
	 * @param sql
	 *            sql语句
	 * @param interceptor
	 * @param exargs
	 *            clazz类中被声明为@Extendable 的字段数组
	 */
	public <T> T queryOne(final Class<T> clazz, final String sql,
			final AssemInterceptor<T> interceptor, final String... exargs) {
		List<T> list = query(clazz, sql, interceptor, exargs);
		return (list.size() < 1) ? null : list.get(0);
	}

	/**
	 * 返回符合某一sql语句的记录
	 * 
	 * @param clazz
	 *            指定的VO类
	 * @param sql
	 *            sql语句
	 * @param exargs
	 *            clazz类中被声明为@Extendable 的字段数组
	 */
	public <T> List<T> query(final Class<T> clazz, final String sql,
			final String... exargs) {
		return query(clazz, sql, null, exargs);
	}

    /**
	 * 返回符合某一sql语句的记录
	 *
	 * @param clazz
	 *            指定的VO类
	 * @param sql
	 *            sql语句
	 * @param interceptor
	 * @param exargs
	 */
	public <T> List<T> query(final Class<T> clazz, final String sql,
			final AssemInterceptor<T> interceptor,final String... exargs){
        return query(clazz,sql,interceptor,null,null,exargs);
    }

	/**
	 * 返回符合某一sql语句的记录
	 * 
	 * @param clazz
	 *            指定的VO类
	 * @param sql
	 *            sql语句
	 * @param interceptor
	 * @param exargs
	 */
	public <T> List<T> query(final Class<T> clazz, final String sql,
			final AssemInterceptor<T> interceptor,final String[] excludeFields,final String[] includeFields, final String... exargs) {
		final List<T> datas = new ArrayList<T>();
		logger.debug("query sql:"+sql);
		getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				T indata;
				String fname = "";
				Method method = null;
				try {
					indata = (T) clazz.newInstance();
					for (Field field : clazz.getDeclaredFields()) {
						if (Modifier.isFinal(field.getModifiers())
								|| Modifier.isStatic(field.getModifiers())) {
							continue;
						}
						fname = field.getName();
						if (field.getAnnotation(Extendable.class) != null
								&& isNotContains(fname, exargs)) {
							continue;
						}

                        if(includeFields!=null &&isNotContains(fname, includeFields)){//不包含在包含表中则跳过
                            continue;
                        }
                        //先过滤列表
                        if(excludeFields!=null && !isNotContains(fname, excludeFields)){//包含在过滤表则不绑定
                            continue;
                        }
						method = clazz.getDeclaredMethod("set"
								+ capitalize(fname), field.getType());
						if (field.getType() == Boolean.TYPE || field.getType()==Boolean.class) {
							method.invoke(indata, rs.getBoolean(fname));
						} else if (field.getType() == Byte.TYPE || field.getType()==Byte.class ) {
							method.invoke(indata, rs.getByte(fname));
						} else if (field.getType() == Short.TYPE || field.getType()==Short.class ) {
							method.invoke(indata, rs.getShort(fname));
						} else if (field.getType() == Integer.TYPE || field.getType()==Integer.class ) {
							method.invoke(indata, rs.getInt(fname));
						} else if (field.getType() == Long.TYPE || field.getType()==Long.class ) {
							method.invoke(indata, rs.getLong(fname));
						} else if (field.getType() == Float.TYPE || field.getType()==Float.class ) {
							method.invoke(indata, rs.getFloat(fname));
						} else if (field.getType() == Double.TYPE || field.getType()==Double.class ) {
							method.invoke(indata, rs.getDouble(fname));
						} else {
							method.invoke(indata, rs.getString(fname));
						}
					}
					if (interceptor != null) {
						interceptor.hold(indata);
					}
					datas.add(indata);
				} catch (Exception ex) {
					throw new RuntimeException("query list error (field: "
							+ fname + ").", ex);
				}
			}
		});
		return datas;
	}

	//by super 2012-10-25 20:07:55 把isNotContains作用域从private 改为protected
	protected static boolean isNotContains(String one, String[] arrays) {
        if(arrays==null){
            return true;
        }
		for (String str : arrays) {
			if (one.equalsIgnoreCase(str))
				return false;
		}
		return true;
	}
	//by super 2012-10-25 20:07:55 把capitalize作用域从private 改为protected
	protected static String capitalize(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * 根据指定list返回一字符串，其中数组元素用逗号隔开。
	 * @param list
	 * 			String类型的List
	 * @return 结果字符串
	 */
	protected static String toString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : list) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append("'").append(s).append("'");
		}
		return sb.toString();
	}

	/**
	 * 根据指定数组返回一字符串，其中数组元素用逗号隔开。
	 * @param arrays
	 * 			String类型的数组
	 * @return 结果字符串
	 */
	protected static String toString(String[] arrays) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : arrays) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append("'").append(s).append("'");
		}
		if (sb.length() < 1)
			return "'null_'";
		return sb.toString();
	}

	/**
	 * 根据指定数组返回一字符串，其中数组元素用逗号隔开。
	 * @param arrays
	 * 			long类型的数组
	 * @return 结果字符串
	 */
	protected static String toString(long[] arrays) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (long s : arrays) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否为空
	 */
	protected boolean isEmpty(String str) {
		return StringTools.isEmpty(str);
	}

	/**
	 * 判断字符串是否为非空
	 */
	protected boolean isNotEmpty(String str) {
		return StringTools.isNotEmpty(str);
	}

    //下面是新添加的方法。by IceRao
    ///////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。
     * @param <T> 实体类型
     * @param object 实体实例
     */
    public <T> void updateObject(T object){
        updateObject(object,null,null,null);
    }
    /**
     * 更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。
     * @param <T> 实体类型
     * @param object 实体实例
     * @param primarykeyName 指定主键字段
     */
    public <T> void updateObject(T object,String primarykeyName){
        updateObject(object,null,null,primarykeyName);
    }

    /**
     * 更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。
     * @param <T>
     * @param object
     * @param excludeFields 包含不更新的字段名。这里对应的是Vo字段名而不是数据库字段名！
     */
    public <T> void updateObjectExcludeFields(T object,String[] excludeFields){
        updateObject(object,excludeFields,null,null);
    }

    /**
     *  更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。
     * @param <T>
     * @param object
     * @param includeFields 指定需要更新的字段名。这里对应的是Vo字段名而不是数据库字段名！
     */
    public <T> void updateObjectIncludeFields(T object,String[] includeFields){
        updateObject(object,null,includeFields,null);
    }
    /**
     * 更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。参数excludeFields和includeFields请只指定一个，两个参数都指定某字段时此字段不会被更新。
     * @param <T> 实体类型
     * @param object 实体实例
     * @param excludeFields 指定不需要更新的字段数组。如果都需要更新时此参数为null。这里对应的是Vo字段名而不是数据库字段名！
     * @param includeFields 指定需要更新的数组。此参数不为空时如果不包含在此参数的字段将不更新。如果不指定包含（null）时默认都更新。这里对应的是Vo字段名而不是数据库字段名！   
     */
    public <T> void updateObject(T object,String[] excludeFields,String[] includeFields){
        updateObject(object,excludeFields, includeFields,null);
    }

    /**
     * 更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。参数excludeFields和includeFields请只指定一个，两个参数都指定某字段时此字段不会被更新。
     * @param <T> 实体类型
     * @param object 实体实例
     * @param excludeFields 指定不需要更新的字段数组。如果都需要更新时此参数为null。这里对应的是Vo字段名而不是数据库字段名！
     * @param includeFields 指定需要更新的数组。此参数不为空时如果不包含在此参数的字段将不更新。如果不指定包含（null）时默认都更新。这里对应的是Vo字段名而不是数据库字段名！
     * @param primarykeyName 指定主键
     */
    public <T> void updateObject(T object,String[] excludeFields,String[] includeFields,String primarykeyName){
        Class<?> clazz = object.getClass();
        PrimaryKeyVo pk=getPrimayKey(clazz);
        String className;
        if(object instanceof ITableName){
            className=((ITableName)object).getTableName();
        }
        else{
        //2009 11 7修改为表名通过提供者来获得
            getTableNameProvider().setDaoClass(clazz);
            className = getTableNameProvider().getTableName(); //clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
        }
        StringBuilder sql=new StringBuilder("update ").append(className).append(" set ");
        StringBuilder sqlValue=new StringBuilder();
        try{
            Field[] fields=clazz.getDeclaredFields();
            String fname;
            Method method;
            Object fieldValue;
            Class type;
            Object keyValue=null;
            for(Field field:fields){
                fname = field.getName();
                if (field.getAnnotation(Extendable.class) != null) {
                	logger.debug(fname+"标记为@Extendable，不更新此字段");
                        continue;
                }
                if(!fname.equalsIgnoreCase(pk.name()) && includeFields!=null && isNotContains(fname, includeFields)){
                	logger.debug(fname+"\"不\"包含在需要更新的参数列表，不更新此字段");
                    continue;
                }
                else if(!isNotContains(fname, excludeFields)){
                	logger.debug(fname+"包含在不需要更新的参数列表，不更新此字段");
                    continue;
                }

                method = clazz.getDeclaredMethod((field.getType().getName().equals("boolean") ? "is" : "get") + capitalize(fname));
                fieldValue=method.invoke(object);
                //对于seqid默认为自增主键
                if(fname.equalsIgnoreCase(pk.name()) && StringTools.isEmpty(primarykeyName)){
                	logger.debug(fname+"为自增主键，不用更新");
                    keyValue=Long.parseLong(fieldValue.toString());
                    continue;
                }

                if(StringTools.isNotEmpty(primarykeyName)){
                    if(primarykeyName.equals(fname)){
                    	logger.debug(fname+"为主键");
                        keyValue=fieldValue;
                    }
                }

                if(StringTools.isNotEmpty(primarykeyName) && primarykeyName.equals(fname)){
                	logger.debug(fname+"为主键，不需要更新");
                    continue;
                }

                type=field.getType();
                if(isNumType(type) ){
                    sqlValue.append(getEacapeFieldName(fname)).append("=").append(fieldValue.toString()).append(",");
                }else if(field.getType().getName().equals("boolean")){
                	sqlValue.append(getEacapeFieldName(fname)).append("=").append(Boolean.valueOf(fieldValue.toString()) ? 1 : 0).append(",");
                }else if(type==Date.class){
                    sqlValue.append(getEacapeFieldName(fname)).append("='").append(DatetimeUtil.formatyyyyMMddHHmmss((Date)fieldValue)).append("',");
                }else{//对于非上面的类型都使用字符串形式更新。对某些数据类型有可能出错
                    sqlValue.append(getEacapeFieldName(fname)).append("='").append(StringTools.escapeSql(fieldValue)).append("',");
                }
            }
            String primarykey=pk.name();
            if(StringTools.isNotEmpty(primarykeyName)){
                primarykey = primarykeyName;
            }
            String finalSql=sql.toString()+sqlValue.substring(0,sqlValue.length()-1)+" where ";
            if(keyValue instanceof String){
                finalSql+= primarykey+"='"+StringTools.escapeSql(keyValue)+" '";
            }
            else{
                finalSql+= primarykey+"="+StringTools.escapeSql(keyValue)+" ";
            }
            //logger.debug("update sql:"+finalSql);
            this.executeUpdate(finalSql);
        }
        catch(Exception ex){
        	logger.error(ex);
            new XLRuntimeException(ex);
        }
    }

    /**
     * 保存某个实体对象。对于标记为@Extendable 的字段不进行持久化的操作。
     * @param <T> 实体类型
     * @param object 实体实例
     * @return 实体实例
     */
    public <T> T saveObject(T object){
        Class<?> clazz = object.getClass();
        String className;
        if(object instanceof ITableName){
            className=((ITableName)object).getTableName();
        }
        else{
            getTableNameProvider().setDaoClass(clazz);
            className = getTableNameProvider().getTableName(); //clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
        }
        StringBuilder sqlColumn=new StringBuilder("(");
        StringBuilder sqlValue=new StringBuilder("(");
        PrimaryKeyVo pk=getPrimayKey(clazz);

        String fname;
        Method method;
        Object fieldValue;
        Class<?> type;
        try{
            Field[] fields=clazz.getDeclaredFields();
            Field field;
            for (int i=0;i<fields.length;i++){
                field=fields[i];
                fname = field.getName();
                if (field.getAnnotation(Extendable.class) != null) {
                	logger.debug(fname+"标记为@Extendable，不保存此字段");
                        continue;
                }
                //对于seqid默认为自增主键
                if(fname.equalsIgnoreCase(pk.name()) && pk.autoIncrement()){
                	logger.debug(fname+"为自增主键，不用插入");
                    continue;
                }

                method = clazz.getDeclaredMethod((field.getType().getName().equals("boolean") ? "is" : "get") + capitalize(fname));
                fieldValue=method.invoke(object);
                sqlColumn.append(getEacapeFieldName(fname));
                type=field.getType();
                if(isNumType(type)){
                    sqlValue.append(fieldValue.toString());
                }else if(type.getName().equals("boolean")){
                	sqlValue.append(Boolean.valueOf(fieldValue.toString()) ? 1 : 0);
                }else if(field.getType()==Date.class){
                    sqlValue.append("'").append(DatetimeUtil.formatyyyyMMddHHmmss((Date)fieldValue)).append("'");
                }else{
                    sqlValue.append("'").append(StringTools.escapeSql(fieldValue)).append("'");
                }
                sqlColumn.append(",");
                sqlValue.append(",");
            }

            String sqlInsert="insert into "+className+ sqlColumn.substring(0,sqlColumn.length()-1) +") values"+sqlValue.substring(0,sqlValue.length()-1)+")" ;
            String sqlGetSeqid="select "+pk.name()+" from "+className+ " order by "+pk.name()+" desc limit 0,1";

            execute(sqlInsert);
            //获得主键
            long seqid=this.getSingleLong(sqlGetSeqid);
            logger.debug("新纪录的"+pk.name()+"为："+seqid);
            method = clazz.getDeclaredMethod("set"+ capitalize(pk.name()),Long.TYPE);
            method.invoke(object, seqid);
        }
        catch(Exception ex){
        	logger.error("", ex);
            new XLRuntimeException(ex);
        }

        return object;
    }

    /**
     * 通过主键删除一个实体。主键规定为seqid。
     * <br/>注意此方法只会将seqid作为删除条件而不会处理其他字段的条件。如果要按照其他字段请使用deleteObjectByCondition(T object,String exwheresql)方法。
     * @param <T> 实体类型
     * @param object 包含主键的实体
     */
    public <T> void deleteObject(T object){
        if(object==null){
            throw new XLRuntimeException("删除的对象不能为空");
        }
        Class clazz = object.getClass();
        PrimaryKeyVo pk=getPrimayKey(clazz);
        String className;
        if(object instanceof ITableName){
            className=((ITableName)object).getTableName();
        }
        else{
        //2009 11 7修改为表名通过提供者来获得
            getTableNameProvider().setDaoClass(clazz);
            className = getTableNameProvider().getTableName(); //clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
        }
        try{
            Method method=clazz.getDeclaredMethod("get"+capitalize(pk.name()));

            String sql= "delete from "+className+" where "+pk.name()+"="+method.invoke(object);
            //logger.debug("delete sql:"+sql);
            this.execute(sql);
        }
        catch(Exception ex){
            throw new XLRuntimeException("没有定义get"+capitalize(pk.name())+"方法",ex);
        }
    }

    /**
     * 删除一个对象。对象的删除条件是通过object的包含有数据的字段去组成的。被标记为@Extendable 的字段不参与条件。
     * @param <T> 删除的对象类型
     * @param object 删除条件对象
     * @param exwheresql 附加的删除用条件
     */
    public <T> void deleteObjectByCondition(T object,String exwheresql){
        Class<?> clazz = object.getClass();
        String className;
        if(object instanceof ITableName){
            className=((ITableName)object).getTableName();
        }
        else{
        //2009 11 7修改为表名通过提供者来获得
            getTableNameProvider().setDaoClass(clazz);
            className = getTableNameProvider().getTableName(); //clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
        }
        //String className = clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
        String sql ="delete from "+className;
        StringBuilder sqlWhere = new StringBuilder(" where 1=1 ");

        String fname = "";
        Method method = null;
        Object fieldValue = null;
        Class<?> type=null;
        try {
            //组成sql语句的条件查询部分
            for (Field field : clazz.getDeclaredFields()) {
                //标记为@Extendable的字段不组入where语句中
                if (field.getAnnotation(Extendable.class) != null) {
                    continue;
                }
                fname = field.getName();
                method = clazz.getDeclaredMethod("get" + capitalize(fname));
                //如果字段不属于下列的类型也不组入where语句
                type=field.getType();
                if ( !isSupportType(type)) {
                    continue;
                }
                fieldValue = method.invoke(object);
                if (type == String.class) {
                    if(StringTools.isNotEmpty((String)fieldValue)){
                        sqlWhere.append(" and ").append(getEacapeFieldName(fname)).append("='").append(StringTools.escapeSql((String) fieldValue)).append("' ");
                    }
                }
                else if(type==Date.class){
                    sqlWhere.append(" and ").append(getEacapeFieldName(fname)).append("='").append(DatetimeUtil.formatyyyyMMddHHmmss( (Date)fieldValue)).append("' ");
                }
                else if (0 != StringTools.tryParseDouble(fieldValue)) {
                    sqlWhere.append(" and ").append(getEacapeFieldName(fname)).append("=").append(fieldValue).append(" ");
                }
            }
            if (StringTools.isNotEmpty(exwheresql)) {
                sqlWhere.append(" and ").append(exwheresql).append(" ");
            }
            sql+=sqlWhere.toString();
            //logger.debug("delete sql:"+sql);
            this.execute(sql);
        }
        catch(Exception ex){
        	logger.debug(sql+sqlWhere.toString());
            throw new XLRuntimeException(ex);
        }
    }

    /**
     * 通用的查询某个对象。此方法调用 {@link #findPagedObjects(Object, String, String, PagedFliper)}
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象。此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param orderbysql 进行排序的语句。例如 name desc,seqid asc.
     * @return 通过查询后得到的所有结果
     */
    public <T> List<T> findObjects(T object, String exwheresql,String orderbysql) {
        return (List<T>) findPagedObjects(object, exwheresql,orderbysql, null).getDatas();
    }

    /**
     * 根据条件查询首个对象
     * @param <T> 对象所属类型
     * @param object 查询的条件。填充seqid来构造查询条件
     * @return 查询得到的首个结果，不存在时返回null
     */
    public <T> T findObject(T object){
        T o=null;
        try{
            Class clazz = object.getClass();
            PrimaryKeyVo pk=getPrimayKey(clazz);
            String className;
            if(object instanceof ITableName){
                className=((ITableName)object).getTableName();
            }
            else{
            //2009 11 7修改为表名通过提供者来获得
                getTableNameProvider().setDaoClass(clazz);
                className = getTableNameProvider().getTableName(); //clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
            }
            //String className = clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
            Method method = clazz.getDeclaredMethod("get"+capitalize(pk.name()));
            Object seqid=method.invoke(object);
            String sql="select * from "+className+" where "+pk.name()+"="+seqid;

            o=(T)this.queryOne(clazz, sql);
        }
        catch(Exception ex){
            new XLRuntimeException(ex);
        }
        return o;
        
    }
    /**
     * 根据条件查询首个对象
     * @param <T> 对象所属类型
     * @param object 查询的条件。此方法会重组所有不空（0，null,@Extendable 不处理）的字段为条件。
     * @return 查询得到的首个结果，不存在时返回null
     */
    public <T> T findObjectByCondition(T object){
        //添加分页对象是为了减少查询的范围，提高效率
        PagedFliper fliper=new PagedFliper();
        fliper.setPageNo(1);
        fliper.setPageSize(1);
        List<T> list=(List<T>)findPagedObjects(object,null,fliper).getDatas();
        if(list!=Collections.EMPTY_LIST){
            return list.get(0);
        }
        return null;
    }

    /**
     * 通用的查询某个对象。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的清空。需要更加复杂的查询时可以使用query(final Class<T>, final String,final AssemInterceptor<T>, final String...)
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjects(T object, String exwheresql, PagedFliper fliper) {
        return this.findPagedObjects(object, exwheresql,null, fliper);
    }

    /**
     * 通用的查询某个对象。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的清空。需要更加复杂的查询时可以使用query(final Class<T>, final String,final AssemInterceptor<T>, final String...)
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param orderbysql 进行排序的语句。例如 name desc,seqid asc.当存在分页对象时以分页对象的为先而不使用此参数。
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjects(T object, String exwheresql,String orderbysql, PagedFliper fliper){
        return findPagedObjects(object,exwheresql,orderbysql,fliper,null,null);
    }

    /**
     * 通用的查询某个对象，可以对特定的列进行绑定指定。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的情况。
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param orderbysql 进行排序的语句。例如 name desc,seqid asc.当存在分页对象时以分页对象的为先而不使用此参数。
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @param includeFields,需要指定进行输出的vo字段，指定此参数时默认不输出，只有这个参数有的VO字段才输出。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjectsIncludeFields(T object, String exwheresql,String orderbysql, PagedFliper fliper,String[] includeFields){
        return findPagedObjects(object, exwheresql, orderbysql, fliper, null, includeFields);
    }

        /**
     * 通用的查询某个对象，可以对特定的列进行绑定指定。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的情况。
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @param includeFields,需要指定进行输出的vo字段，指定此参数时默认不输出，只有这个参数有的VO字段才输出。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjectsIncludeFields(T object, PagedFliper fliper,String[] includeFields){
        return findPagedObjects(object, null, null, fliper, null, includeFields);
    }

    /**
     * 通用的查询某个对象，可以对特定的vo字段进行不绑定指定。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的情况。
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param orderbysql 进行排序的语句。例如 name desc,seqid asc.当存在分页对象时以分页对象的为先而不使用此参数。
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @param excludeFields，需要进行过滤不输出的vo字段，指定此参数是默认都输出，包含在此过滤参数的才不输出。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjectsExcludeFields(T object, String exwheresql,String orderbysql, PagedFliper fliper,String[] excludeFields){
        return findPagedObjects(object, exwheresql, orderbysql, fliper, excludeFields, null);
    }

        /**
     * 通用的查询某个对象，可以对特定的vo字段进行不绑定指定。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的情况。
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @param excludeFields，需要进行过滤不输出的vo字段，指定此参数是默认都输出，包含在此过滤参数的才不输出。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjectsExcludeFields(T object, PagedFliper fliper,String[] excludeFields){
        return findPagedObjects(object, null, null, fliper, excludeFields, null);
    }

    /**
     * 通用的查询某个对象。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的情况。需要更加复杂的查询时可以使用query(final Class<T>, final String,final AssemInterceptor<T>, final String...)
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param orderbysql 进行排序的语句。例如 name desc,seqid asc.当存在分页对象时以分页对象的为先而不使用此参数。
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @param excludeFields，需要进行过滤不输出的vo字段，指定此参数是默认都输出，包含在此过滤参数的才不输出。
     * @param includeFields,需要指定进行输出的vo字段，指定此参数时默认不输出，只有这个参数有的VO字段才输出。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjects(T object, String exwheresql,String orderbysql, PagedFliper fliper,String[] excludeFields,String[] includeFields) {
        Class clazz = object.getClass();
        DataAccessReturn<T> datas = DataAccessReturn.EMPTY;
        //在这下面组sql查询语句
        String className;
        if(object instanceof ITableName){
            className=((ITableName)object).getTableName();
        }
        else{
        //2009 11 7修改为表名通过提供者来获得
            getTableNameProvider().setDaoClass(clazz);
            className = getTableNameProvider().getTableName(); //clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
        }
        //String className = clazz.toString().substring(clazz.toString().lastIndexOf('.') + 1).toLowerCase();
        StringBuilder queryFields=new StringBuilder("select ");//保存要进行查询的字段vv
        StringBuilder sql = new StringBuilder(" from ");
        StringBuilder sqlCount = new StringBuilder("select count(1) from ");
        sql.append(className);
        sqlCount.append(className);

        StringBuilder sqlWhere = new StringBuilder(" where 1=1 ");

        String fname = "";
        Method method = null;
        Object fieldValue = null;
        Class type=null;
        try {
            //组成sql语句的条件查询部分
            for (Field field : clazz.getDeclaredFields()) {
                //标记为@Extendable的字段不组入where语句中
                if (field.getAnnotation(Extendable.class) != null) {
                    continue;
                }
                fname = field.getName();
                String getterName = (field.getType().getName().equals("boolean") ? "is" : "get") + capitalize(fname);
                try{
                	method = clazz.getDeclaredMethod(getterName);
                } catch(NoSuchMethodException e){
                	logger.warn("can not find getter : " + getterName);
                	continue;
                }
                if(excludeFields==null && includeFields==null){
                    //logger.debug(fname+"不包含在过滤参数，输出到 select 子句");
                    queryFields.append(getEacapeFieldName(fname)).append(",");
                }
                else{
                    if(excludeFields!=null && !isNotContains(fname, excludeFields)){
                        //logger.debug(fname+"包含在排除参数，不输出到 select 子句");
                    }
                    else if(includeFields!=null && isNotContains(fname, includeFields)){//不过滤或者过包含
                        //logger.debug(fname+"不包含在包含参数，不输出到 select 子句");

                    }
                    else{
                        //logger.debug(fname+"需要输出到select子句");
                        queryFields.append(getEacapeFieldName(fname)).append(",");
                    }
                }

                //如果字段不属于下列的类型也不组入where语句
                type=field.getType();
                if (!isSupportType(type)) {
                    continue;
                }
                fieldValue = method.invoke(object);
                if(fieldValue==null){
                    continue;
                }
                if (type == String.class) {
                    if(StringTools.isNotEmpty((String)fieldValue)){
                        sqlWhere.append(" and ").append(getEacapeFieldName(fname)).append("='").append(StringTools.escapeSql((String) fieldValue)).append("' ");
                    }
                }
                else if(Date.class==type){
                    sqlWhere.append(" and ").append(getEacapeFieldName(fname)).append("='").append(DatetimeUtil.formatyyyyMMddHHmmss((Date) fieldValue)).append("' ");
                }
                else if (0 != StringTools.tryParseDouble(fieldValue)) {
                    sqlWhere.append(" and ").append(getEacapeFieldName(fname)).append("=").append(fieldValue).append(" ");
                }
            }
            if (StringTools.isNotEmpty(exwheresql)) {
                sqlWhere.append(" and ").append(exwheresql).append(" ");
            }

            sqlCount.append(sqlWhere);
            sql.append(sqlWhere);

            String fieldString=queryFields.toString();
            fieldString=fieldString.substring(0, fieldString.length()-1);

            sql=new StringBuilder(fieldString).append(sql);//将select fields填入串的前面

            int rowCount = 0;//假设行不为0，不分页是需要。
            List<T> list = null;
            //分页时的情况，需要进行2次的查询。
            if (fliper != null) {
            	logger.debug("对" + className + "进行分页查询");
                //logger.debug("find sqlCount:" + sqlCount.toString());
                if (fliper.isNotEmptySortColumn()) {
                    sql.append(" order by ").append(fliper.getSortColumn());
                }
                rowCount = getSingleInt(sqlCount.toString());
                sql.append(fliper.limitsql(rowCount));
                //logger.debug("find sql:" + sql.toString());
                if (rowCount == 0) {
                    list = Collections.EMPTY_LIST;
                } else {
                    list = query(clazz, sql.toString(),null,excludeFields,includeFields);
                }
            } else {
            	logger.debug("对" + className + "进行不分页的查询");
                if(StringTools.isNotEmpty(orderbysql)){
                    sql.append(" order by ").append(orderbysql);
                }
                //logger.debug("find sql:" + sql.toString());
                list = query(clazz, sql.toString(),null,excludeFields,includeFields);
                rowCount = list.size();
                if (rowCount == 0) {
                    list = Collections.EMPTY_LIST;
                }
            }

            datas = new DataAccessReturn<T>(rowCount, list);
        } catch (Exception ex) {
        	logger.error("", ex); 
            throw new XLRuntimeException("current sql:" + sql.toString(), ex);
        }

        return datas;
    }

    /**
     * 获得一个表中最大的可用sn。
     * @param tableName 表格名
     * @param fieldName 字段名
     * @param fieldLength 这个字段编号的总长度
     * @return 格式化好的可用的sn
     */
    public String getMaxNewSn(String tableName,String fieldName,int fieldLength){
        return getNewSn(tableName, fieldName, fieldLength, null, null, true, 0);
    }

    /**
     * 获得一个表中特定域的最小可用sn。这个sn的获取是通过查询中间可用值得到的。
     * @param tableName 表格名
     * @param fieldName 字段名
     * @param fieldLength 这个字段编号的总长度（包含头部和尾部）
     * @param noMaxStep 当不是从最大值开始获取时用于中间查询的记录递增值
     * @return 格式化好的可用的sn
     */
    public String getSmoothNewSn(String tableName,String fieldName,int fieldLength,int noMaxStep){
        return getNewSn(tableName, fieldName, fieldLength, null, null, false, noMaxStep);
    }

    /**
     * 获得一个表中特定域的可用sn。sn可以包含字符串的头部和尾部，sn数字编号起步至少应该以1起步而不应该是0.
     * @param tableName 表格名
     * @param fieldName 字段名
     * @param fieldLength 这个字段编号的总长度（包含头部和尾部）
     * @param snprefix 编号的头部
     * @param sntail 编号的尾部
     * @param maxFirst 是否取最大值
     * @return 格式化好的可用的sn
     */
    public String getNewSn(String tableName,String fieldName,int fieldLength,String snprefix,String sntail,boolean maxFirst){
        return getNewSn(tableName, fieldName, fieldLength, snprefix, sntail, maxFirst, 100);
    }

    /**
     * 获得一个表中特定域的可用sn。sn可以包含字符串的头部和尾部，sn数字编号起步至少应该以1起步而不应该是0.
     * @param tableName 表格名
     * @param fieldName 字段名
     * @param fieldLength 这个字段编号的总长度（包含头部和尾部）
     * @param snprefix 编号的头部
     * @param sntail 编号的尾部
     * @param maxFirst 是否取最大值
     * @param noMaxStep 当不是从最大值开始获取时用于中间查询的记录递增值
     * @return 格式化好的可用的sn
     */
    public String getNewSn(String tableName,String fieldName,int fieldLength,String snprefix,String sntail,boolean maxFirst,int noMaxStep){
        String newSn=null;
        if(snprefix==null){
            snprefix="";
        }
        if(sntail==null){
            sntail="";
        }
        int prefixLength=0;
        int tailLength=0;
        prefixLength=snprefix.length();
        tailLength=sntail.length();

        if(maxFirst){
            //String sql="select "+fieldName+" from "+tableName+" order by "+fieldName+" desc limit 0,1";
            String sql="select max("+fieldName+") from "+tableName;
            String sn=this.getSingleString(sql);
            if(StringTools.isNotEmpty(sn)){
                sn=sn.substring(0,sn.length()-sntail.length()).substring(snprefix.length());
            }
            else{
                sn="0";
            }
            long newsnValue=Long.parseLong(sn)+1;

            newSn=snprefix+StringTools.toLenString(newsnValue,fieldLength-prefixLength-tailLength)+sntail;
        }
        else{
            if(noMaxStep==0){
                noMaxStep=100;
            }
            int currentPageNo=0;
            //String lastStringValue=snprefix+StringTools.toLenString(0,fieldLength-prefixLength-tailLength)+sntail;
            long lastValue=0;
            long currentValue;
            boolean found=false;
            List<StringInfo> stringInfo;
            while(!found){
                String sql="select "+fieldName+" as fieldValue from "+tableName+" order by "+fieldName+" asc limit "+currentPageNo*noMaxStep+","+noMaxStep;
                stringInfo=this.query(StringInfo.class, sql);
                if(stringInfo.size()>0){
                    for(StringInfo si:stringInfo){
                        currentValue=Long.parseLong(si.getFieldValue().substring(0,si.getFieldValue().length()-sntail.length()).substring(snprefix.length()));
                        if(currentValue>lastValue+1){//找到一个空位，跳出循环
                            found=true;
                            break;
                        }
                        else{
                            lastValue=currentValue;
                        }
                    }
                }
                else{//已经没有记录了，跳出循环
                    break;
                }
                currentPageNo++;
            }

            newSn=snprefix+StringTools.toLenString(lastValue+1,fieldLength-prefixLength-tailLength)+sntail;
        }
        
        return newSn;
    }

    /**
     * 通过字符串数组生成用于in子句的字符串
     * @param strArray
     * @return
     */
    protected static String uniteForIn(String[] strArray) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
        if(strArray==null || strArray.length==0){//由于字符串数组为空时会使得sql查询抛出异常
        	logger.debug("组成in子句时的字符串数组为空，如果为()会产生sql错误，替换成(null)");
            sb.append("null");
        }
        else{
            for (int i = 0; i < strArray.length; i++) {
                if (i != 0)
                    sb.append(",");
                sb.append("'").append(StringTools.escapeSql(strArray[i])).append("'");
            }
        }
		sb.append(")");
		return sb.toString();
	}

    /**
     * 此方法用于Spring2.5自动扫描时注入数据源用。如果需要手动设置数据源时调用{@link #setDataSource(DataSource)}方法。
     * 如果数据源名称不为dataSource的话，请重写这个方法。
     * @param dataSource 数据源
     */
    @Resource(name="dataSource")
    public void setDataS(DataSource dataSource){
        this.setDataSource(dataSource);
        logger.debug("在"+this.getClass().getName()+",dataSource被注入");
    }

    /**
     * 判断指定类型是否被生成sql时所支持
     * @param type
     * @return
     */
    public boolean isSupportType(Class type) {
        if(type==String.class){
            return true;
        }
        if(isNumType(type)){
            return true;
        }
        if( type == Date.class){
            return true;
        }
        return false;
    }

    /**
     * 判断类似是否是数值型
     * @param type
     * @return
     */
    public boolean isNumType(Class type){
        if(type == Short.TYPE || type==Short.class){
            return true;
        }
        if(type == Integer.TYPE || type==Integer.class){
            return true;
        }
        if(type == Long.TYPE || type==Long.class ){
            return true;
        }
        if(type == Float.TYPE || type==Float.class){
            return true;
        }
        if(type == Double.TYPE || type==Double.class){
            return true;
        }
        if( type == Date.class){
            return true;
        }
        return false;
    }

    /**
     * 获得主键注解对象实体
     * 即使对应的vo没有注解也会返回一个默认的注解（seqid,auto_increament）
     * @param clazz
     * @return
     */
    private PrimaryKeyVo getPrimayKey(Class<?> clazz){
        String primaykeyName="seqid";
        PrimaryKey pk =null;
        Field[] fields = clazz.getDeclaredFields();
        for(Field f:fields){
            pk= f.getAnnotation(PrimaryKey.class);
            if(pk!=null){
                primaykeyName=f.getName();
                break;
            }
        }
        PrimaryKeyVo vo;
        if(pk!=null){
            vo=new PrimaryKeyVo(primaykeyName,pk.autoIncrement());
        }
        else{
            vo=new PrimaryKeyVo("seqid",true);
        }
        return vo;
    }

    private String getEacapeFieldName(String fieldName){
        return "`"+fieldName+"`";
    }

    class PrimaryKeyVo{
        private String name;
        private boolean autoIncrement;
        public PrimaryKeyVo(String name,boolean autoIncrement){
            this.name=name;
            this.autoIncrement=autoIncrement;
        }
        public String name(){
            return name;
        }
        public boolean autoIncrement(){
            return autoIncrement;
        }
    }
}


package com.xunlei.common.dao;

import java.util.List;
import java.util.Map;

import com.xunlei.common.util.PagedFliper;
import com.xunlei.common.web.bean.DataAccessReturn;

/**
 * 此接口公布一些常用的数据库查询方法。如果一个dao需要公布这些方法，请在dao的对应接口继承此接口。
 * @author IceRao
 */
public interface ICommonDao {
	/**
	 * 执行某一sql语句，返回一int数据
	 */
    public int getSingleInt(String sql);
    /**
	 * 执行某一sql语句，返回一long数据
	 */
    public long getSingleLong(String sql);
    /**
	 * 执行某一sql语句，返回一double数据
	 */
    public double getSingleDouble(String sql);
    /**
	 * 执行某一sql语句，返回一字符串数据
	 */
    public String getSingleString(String sql);
    /**
	 * 插入某一条记录
	 * 
	 * @param data
	 *            VO数据类
	 */
	public void insertObject(Object data);
	/**
     * 更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。
     * @param <T> 实体类型
     * @param object 实体实例
     */
    public <T> void updateObject(T object);
    /**
     * 更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。
     * @param <T>
     * @param object
     * @param excludeFields 包含不更新的字段名。这里对应的是Vo字段名而不是数据库字段名！
     */
    public <T> void updateObjectExcludeFields(T object,String[] excludeFields);
    /**
     *  更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。
     * @param <T>
     * @param object
     * @param includeFields 指定需要更新的字段名。这里对应的是Vo字段名而不是数据库字段名！
     */
    public <T> void updateObjectIncludeFields(T object,String[] includeFields);
    /**
     * 更新某个实体。实体必须包含用于标识此实体的seqid，否则更新会失败。标识为@Extendable 的字段不会被更新。参数excludeFields和includeFields请只指定一个，两个参数都指定某字段时此字段不会被更新。
     * @param <T> 实体类型
     * @param object 实体实例
     * @param excludeFields 指定不需要更新的字段数组。如果都需要更新时此参数为null。这里对应的是Vo字段名而不是数据库字段名！
     * @param includeFields 指定需要更新的数组。此参数不为空时如果不包含在此参数的字段将不更新。如果不指定包含（null）时默认都更新。这里对应的是Vo字段名而不是数据库字段名！
     */
    public <T> void updateObject(T object,String[] excludeFields,String[] includeFields);
    /**
     * 保存某个实体对象。对于标记为@Extendable 的字段不进行持久化的操作。
     * @param <T> 实体类型
     * @param object 实体实例
     * @return 实体实例
     */
    public <T> T saveObject(T object);
    /**
     * 通过主键删除一个实体。主键规定为seqid。
     * <br/>注意此方法只会将seqid作为删除条件而不会处理其他字段的条件。如果要按照其他字段请使用deleteObjectByCondition(T object,String exwheresql)方法。
     * @param <T> 实体类型
     * @param object 包含主键的实体
     */
    public <T> void deleteObject(T object);
    /**
     * 删除一个对象。对象的删除条件是通过object的包含有数据的字段去组成的。被标记为@Extendable 的字段不参与条件。
     * @param <T> 删除的对象类型
     * @param object 删除条件对象
     * @param exwheresql 附加的删除用条件
     */
    public <T> void deleteObjectByCondition(T object,String exwheresql);
	/**
	 * 批量删除某张表指定的seqid记录
	 * 
	 * @param table
	 *            表名
	 * @param seqids
	 *            指定的seqid
	 */
	public void deleteObject(String table, long... seqids);
	/**
     * 通用的查询某个对象。此方法调用 {@link #findPagedObjects(Object, String, String, PagedFliper)}
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象。此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param orderbysql 进行排序的语句。例如 name desc,seqid asc.
     * @return 通过查询后得到的所有结果
     */
    public <T> List<T> findObjects(T object, String exwheresql,String orderbysql);
    /**
     * 根据条件查询首个对象
     * @param <T> 对象所属类型
     * @param object 查询的条件。填充seqid来构造查询条件
     * @return 查询得到的首个结果，不存在时返回null
     */
    public <T> T findObject(T object);
    /**
     * 根据条件查询首个对象
     * @param <T> 对象所属类型
     * @param object 查询的条件。此方法会重组所有不空（0，null,@Extendable 不处理）的字段为条件。
     * @return 查询得到的首个结果，不存在时返回null
     */
    public <T> T findObjectByCondition(T object);
    /**
     * 通用的查询某个对象。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的清空。需要更加复杂的查询时可以使用query(final Class<T>, final String,final AssemInterceptor<T>, final String...)
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjects(T object, String exwheresql, PagedFliper fliper);
    /**
     * 通用的查询某个对象。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的清空。需要更加复杂的查询时可以使用query(final Class<T>, final String,final AssemInterceptor<T>, final String...)
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param exwheresql 附加的查询条件。例如需要区间的查询条件时可以这里添加。例如 and balancedate>='2008-10-20' and balancedate<='2008-10-30'
     * @param orderbysql 进行排序的语句。例如 name desc,seqid asc.当存在分页对象时以分页对象的为先而不使用此参数。
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjects(T object, String exwheresql,String orderbysql, PagedFliper fliper);
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
    public <T> DataAccessReturn<T> findPagedObjectsIncludeFields(T object, String exwheresql,String orderbysql, PagedFliper fliper,String[] includeFields);
    /**
     * 通用的查询某个对象，可以对特定的列进行绑定指定。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的情况。
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @param includeFields,需要指定进行输出的vo字段，指定此参数时默认不输出，只有这个参数有的VO字段才输出。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjectsIncludeFields(T object, PagedFliper fliper,String[] includeFields);
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
    public <T> DataAccessReturn<T> findPagedObjectsExcludeFields(T object, String exwheresql,String orderbysql, PagedFliper fliper,String[] excludeFields);
    /**
     * 通用的查询某个对象，可以对特定的vo字段进行不绑定指定。在传入分页对象时可以将数据进行分页返回。被标记为@Extendable 的字段不会被填充。本方法适合通用的情况。
     * @param <T> 进行操作的数据类型
     * @param object 进行操作的对象,此对象不能为空！（反射时需要）。如果要得到没有条件的对象则传入一个初始对象即可。会更加此实体对象的值进行查询条件的重组。支持String和数值类型。标记为@Extendable 的字段或者类型不是上述的组成查询条件时将被忽略
     * @param fliper 分页对象。如果此项为null时则返回所有的结果。
     * @param excludeFields，需要进行过滤不输出的vo字段，指定此参数是默认都输出，包含在此过滤参数的才不输出。
     * @return 通过查询后得到的分页结果
     */
    public <T> DataAccessReturn<T> findPagedObjectsExcludeFields(T object, PagedFliper fliper,String[] excludeFields);
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
    public <T> DataAccessReturn<T> findPagedObjects(T object, String exwheresql,String orderbysql, PagedFliper fliper,String[] excludeFields,String[] includeFields);
    /**
     * 获得一个表中最大的可用sn。
     * @param tableName 表格名
     * @param fieldName 字段名
     * @param fieldLength 这个字段编号的总长度
     * @return 格式化好的可用的sn
     */
    public String getMaxNewSn(String tableName,String fieldName,int fieldLength);
    /**
     * 获得一个表中特定域的最小可用sn。这个sn的获取是通过查询中间可用值得到的。
     * @param tableName 表格名
     * @param fieldName 字段名
     * @param fieldLength 这个字段编号的总长度（包含头部和尾部）
     * @param noMaxStep 当不是从最大值开始获取时用于中间查询的记录递增值
     * @return 格式化好的可用的sn
     */
    public String getSmoothNewSn(String tableName,String fieldName,int fieldLength,int noMaxStep);
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
    public String getNewSn(String tableName,String fieldName,int fieldLength,String snprefix,String sntail,boolean maxFirst);
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
    public String getNewSn(String tableName,String fieldName,int fieldLength,String snprefix,String sntail,boolean maxFirst,int noMaxStep);
    
    /**
     * 判断指定类型是否被生成sql时所支持
     * @param type
     * @return
     */
    public boolean isSupportType(Class type);

    /**
     * 判断类似是否是数值型
     * @param type
     * @return
     */
    public boolean isNumType(Class type);
    /**
	 * 执行批量的sql语句
	 */
	public int[] batchUpdate(String... sqls);
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
	public String createNextId(String table, String idname, int len);
	/**
	 * 根据某一sql语句返回记录列表
	 */
	public <T> List<T> queryToList(String sql);
	/**
	 * 根据某一sql语句返回记录Map对象
	 */
	public <K, V> Map<K, V> queryToMap(String sql);
	
	public <K, V> Map<K, V> queryToMap(String sql, K key, V value);
	/**
	 * 执行更新的sql语句，返回更新成功的记录数
	 */
	public int executeUpdate(String sql);
	/**
	 * 执行某一sql语句
	 */
	public void execute(String sql);
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
			final String... exargs);

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
			final String... exargs);
}

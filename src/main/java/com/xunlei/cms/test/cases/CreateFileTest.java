/**
 * 
 */
package com.xunlei.cms.test.cases;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xunlei.cms.core.CreateFileConfig;
import com.xunlei.cms.core.CreateFileUtils;

/**
 * @author Brice Li
 *
 */
public class CreateFileTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.xunlei.cms.core.CreateFileUtils#generatePage(com.xunlei.cms.core.CreateFileConfig, java.util.Map)}.
	 * @throws Exception 
	 */
	@Test
	public void testGeneratePage() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("aaa", "我是贱人汉字");
		
		CreateFileConfig config = new CreateFileConfig("utf8", 
				"utf8",
				"D:\\Web Code\\xunlei-web-framework\\xllib4\\webroot\\cmbus\\jinjuan\\template\\inner_index.html",
				"D:\\upload\\jinjuan\\inner_index.html");
		CreateFileUtils.generatePage(config, map);
	}
}

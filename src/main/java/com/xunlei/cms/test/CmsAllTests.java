package com.xunlei.cms.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class CmsAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(CmsAllTests.class.getName());
		//$JUnit-BEGIN$
//		suite.addTest(new CreateFileTest());
		//$JUnit-END$
		return suite;
	}
}

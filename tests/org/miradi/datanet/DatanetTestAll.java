/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DatanetTestAll extends TestCase
{

	public static void runTests()
	{
		junit.textui.TestRunner.run (suite());
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite("All eAM Tests");
		suite.addTest(new TestSuite(TestDatanet.class));
		suite.addTest(new TestSuite(TestDatanetSchema.class));
		suite.addTest(new TestSuite(TestLinkageInstance.class));
		suite.addTest(new TestSuite(TestLinkageType.class));
		suite.addTest(new TestSuite(TestRecordInstance.class));
		suite.addTest(new TestSuite(TestRecordType.class));
		return suite;
	}
}

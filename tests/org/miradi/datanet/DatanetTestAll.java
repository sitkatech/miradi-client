/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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

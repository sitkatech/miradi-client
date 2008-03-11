/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectdata;

import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;

public class TestIntegerData extends EAMTestCase
{
	public TestIntegerData(String name)
	{
		super(name);
	}

	public void testReadingFloatingValues() throws Exception
	{
		IntegerData data = new IntegerData("tag");
		try
		{
			EAM.setLogToString();
			data.set("1.0");
			assertEquals("1", data.get());
			data.set("2.2");
			assertEquals("2", data.get());
			assertContains("Didn't warn for non-int double?", "TRUNCATING", EAM.getLoggedString());

			try
			{
				data.set("2.not a number");
				fail("should have thrown for not a number");
			}
			catch(NumberFormatException ignoreExpected)
			{
			}
		}
		finally
		{
			EAM.setLogToConsole();
		}

	}
}

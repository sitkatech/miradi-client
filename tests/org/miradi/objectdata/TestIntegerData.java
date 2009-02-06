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

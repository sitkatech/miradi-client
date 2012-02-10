/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.martus.util.TestCaseEnhanced;

public class TestNumberData extends TestCaseEnhanced
{
	public TestNumberData(String name)
	{
		super(name);
	}

	public void testFormatting() throws Exception
	{
		NumberData data = new NumberData("tag");
		data.set("5E7");
		assertEquals("Didn't avoid scientific notation?", "50000000", data.get());
		data.set("1.23456");
		assertEquals("Didn't keep decimals?", "1.23456", data.get());
		data.set("0");
		assertEquals("Didn't keep zero as zero?", "0", data.get());
		data.set(".1");
		assertEquals("Didn't handle .1 properly?", "0.1", data.get());
	}
	
}

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

package org.miradi.utils;

import org.miradi.main.EAMTestCase;

public class TestFloatingPointFormatter extends EAMTestCase
{
	public TestFloatingPointFormatter(String name)
	{
		super(name);
	}
	
	public void testFormatEditableValue()
	{
		assertEquals("", FloatingPointFormatter.formatEditableValue(new OptionalDouble()));
		assertEquals("0", FloatingPointFormatter.formatEditableValue(new OptionalDouble(0.0))); 
		assertEquals("0.9", FloatingPointFormatter.formatEditableValue(new OptionalDouble(0.9)));
		assertEquals("0.09", FloatingPointFormatter.formatEditableValue(new OptionalDouble(0.09)));
		assertEquals("0.009", FloatingPointFormatter.formatEditableValue(new OptionalDouble(0.009)));
		assertEquals("0.009", FloatingPointFormatter.formatEditableValue(new OptionalDouble(0.0089)));
		assertEquals("0.001", FloatingPointFormatter.formatEditableValue(new OptionalDouble(0.000999999999999999999)));
		
		assertEquals("formatted using scientific value?", "100000000000000", FloatingPointFormatter.formatEditableValue(new OptionalDouble(100000000000000.0)));
	}
}

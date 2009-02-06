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
package org.miradi.utils;

import org.miradi.main.EAMTestCase;
import org.miradi.rtf.RtfWriter;

public class TestRtfWriter extends EAMTestCase
{

	public TestRtfWriter(String name)
	{
		super(name);
	}
	
	public void testToHex()
	{
		verifyHex("00", (byte) 0x00);
		verifyHex("01", (byte) 0x01);
		verifyHex("0f", (byte) 0x0f);
		verifyHex("10", (byte) 0x10);
		verifyHex("7f", (byte) 0x7f);
		verifyHex("80", (byte) 0x80);
		verifyHex("ff", (byte) 0xff);
	}
	
	private void verifyHex(String expectedValue, byte b)
	{
		assertEquals("byte does not equal hex value?", expectedValue, RtfWriter.toHex(b));
	}
	
	public void testEcode()
	{
		verifyEncoding("a", "a");
		verifyEncoding("\\{", "{");
		verifyEncoding("\\}", "}");
		verifyEncoding("\\\\", "\\");
		
		final char UNICODE_OMEGA = 0x2126;
		verifyEncoding("\\u8486\\~\n", Character.toString(UNICODE_OMEGA));
	}
	
	private void verifyEncoding(String expectedValue, String value)
	{
		assertEquals("value was not encoded properly?", expectedValue, RtfWriter.encode(value));
	}
	
	public void testGetScale()
	{ 
		verifyScale(100, 0, 0);
		verifyScale(100, 50, 50);
		verifyScale(100, 100, 100);
		verifyScale(50, 1600, 500);
		verifyScale(50, 700, 1200);
		verifyScale(10, 8000, 500);
	}
	
	private void verifyScale(int expectedScale, int imageWidth, int imageHeight)
	{
		assertEquals("wrong scale calculated?", expectedScale, RtfWriter.calculateScale(imageWidth, imageHeight));
	}
}

/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

public class TestAbstractUserStringDataWithHtmlFormatting extends TestCaseEnhanced
{
	public TestAbstractUserStringDataWithHtmlFormatting(String name)
	{
		super(name);
	}
	
	public void testSet() throws Exception
	{
		String legalHtml = "Testing<br/>More";
		String illegalHtml = "<html>" + legalHtml + "\n\n";
		MultiLineUserTextData data = new MultiLineUserTextData("tag");
		data.set(illegalHtml);
		assertEquals("Didn't strip out illegal stuff?", legalHtml, data.get());
	}

	public void testIsCurrentValue() throws Exception
	{
		MultiLineUserTextData data = new MultiLineUserTextData("tag");
		String legalHtml = "Testing<br/>";
		data.set(legalHtml);
		assertTrue("Identical compare failed?", data.isCurrentValue(legalHtml));
		
		String disallowedHtml = "<html>" + legalHtml;
		data.set(disallowedHtml);
		assertTrue("Didn't strip tags?", data.isCurrentValue(legalHtml));
		
		String htmlWithNewline = legalHtml + "\n";
		data.set(htmlWithNewline);
		assertTrue("Didn't strip newline?", data.isCurrentValue(legalHtml));
	}
}

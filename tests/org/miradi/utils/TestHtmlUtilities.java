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

package org.miradi.utils;

import org.miradi.main.MiradiTestCase;

public class TestHtmlUtilities extends MiradiTestCase
{
	public TestHtmlUtilities(String name)
	{
		super(name);
	}
	
	public void testReplaceHtmlTags()
	{
		verifyReplacingTags("sometext<br />", "br", "", "sometext");
		verifyReplacingTags("sometext<br/>", "br", "", "sometext");
		verifyReplacingTags("sometext<br>", "br", "", "sometext");
	}
	
	public void testStripHtmlTags()
	{
		final String[] htmlTagsToStrip = new String[]{"html", "body", };
		assertEquals("html tags were not stripped?", "someText", HtmlUtilities.stripHtmlTags("<html><body>someText<body/><html/>", htmlTagsToStrip));
	}
	
	public void testStripHtmlTag()
	{
		verifyStringHtmlTag("<html>someText</html>", "html", "someText");
		verifyStringHtmlTag("<html>someText<html/>", "html", "someText");
		verifyStringHtmlTag("<HTML>someText<HTML/>", "html", "someText");
		verifyStringHtmlTag("<html>someText<html/>", "body", "<html>someText<html/>");
		verifyStringHtmlTag("<html><body>someText<body/><html/>", "body", "<html>someText<html/>");
		verifyStringHtmlTag("<html><body><h1><font size=\"5\">someText<body/><html/>", "font", "<html><body><h1>someText<body/><html/>");
	}
	
	public void testStripAllHtmlTags()
	{
		verifyStringHtmlTags("", "");
		verifyStringHtmlTags("<html>", "");
		verifyStringHtmlTags("<html><body><h1><br/>someText<body/><html/>", "someText");
		verifyStringHtmlTags("<html><body><h1><font size=\"5\">someText<body/><html/>", "someText");
	}

	private void verifyStringHtmlTags(String textWithHtmlTags, String expectedValue)
	{
		assertEquals("html tags were not stripped correctly?", expectedValue, HtmlUtilities.stripAllHtmlTags(textWithHtmlTags));
	}
	
	private void verifyStringHtmlTag(String textWithHtmlTags, String htmlTag, String expectedValue)
	{
		assertEquals("html tag was nore stripped correctly", expectedValue, HtmlUtilities.stripHtmlTag(textWithHtmlTags, htmlTag));
	}
	
	private void verifyReplacingTags(final String htmlText, final String tagToReplace, final String replacement, final String expectedValue)
	{
		assertEquals("html tag was not replaced?", expectedValue, HtmlUtilities.replaceHtmlTags(htmlText, tagToReplace, replacement));
	}
}

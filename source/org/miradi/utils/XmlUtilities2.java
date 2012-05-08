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

import org.apache.commons.lang3.StringEscapeUtils;
import org.miradi.main.EAM;

public class XmlUtilities2
{
	public static String convertXmlTextToPlainText(String value)
	{
		return StringEscapeUtils.unescapeXml(value);
	}

	public static String getXmlEncoded(String value)
	{
		return StringEscapeUtils.escapeXml(value);
	}

	public static String getXmlEncodedApostrophes(String value)
	{
		return value.replaceAll("'", "&apos;");
	}

	public static String convertXmlTextToHtml(final String value)
	{
		String html = XmlUtilities2.convertXmlTextToHtmlWithoutHtmlTags(value);
		html = HtmlUtilities.wrapInHtmlTags(html);

		return html;
	}
	
	public static String convertXmlTextToHtmlWithoutHtmlTags(final String value) throws RuntimeException
	{
		try
		{
			throwIfInvalidXmlWithHtmlTags(value);
		}
		catch(Exception e)
		{
			EAM.logError("Invalid XML: " + value);
			EAM.logException(e);
		}

		return decodeApostrophes(value);
	}

	private static String decodeApostrophes(String value)
	{
		value = value.replaceAll("&#39;", "'");
		value = value.replaceAll("&#x27;", "'");
		value = value.replaceAll("&apos;", "'");
		
		return value;
	}
	
	public static void throwIfInvalidXmlWithHtmlTags(final String value)
	{
		if (value == null)
			return;
		
		String valueWithoutHtmlTags = HtmlUtilities.stripAllHtmlTags(value);
		//NOTE: this safety mechanism might be a speed concern
		if (!isValidXmlWithNoHtmlTags(valueWithoutHtmlTags))
			throw new RuntimeException("Invalid xml value =" + valueWithoutHtmlTags);
	}
	
	private static boolean isValidXmlWithNoHtmlTags(final String value)
	{
		final String decodedValue = convertXmlTextToPlainText(value);
		final String encodedValue = getXmlEncoded(decodedValue);
		
		return encodedValue.equals(value);
	}
	
	public static boolean hasEncoded(final String value)
	{
		if (value.contains("&amp;"))
			return true;
		
		if (value.contains("&lt;"))
			return true;
		
		if (value.contains("&gt;"))
			return true;
		
		if (value.contains("&apos;"))
			return true;
		
		if (value.contains("&quot;"))
			return true;
		
		return false;
	}
}

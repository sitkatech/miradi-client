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

public class XmlUtilities2
{
	public static String getXmlDecoded(String value)
	{
		value = value.replaceAll("&#60;", "<");
		value = value.replaceAll("&#x3c;", "<");
		value = value.replaceAll("&#x3C;", "<");
		value = value.replaceAll("&lt;", "<");

		value = value.replaceAll("&gt;", ">");
		value = value.replaceAll("&#x3E;", ">");
		value = value.replaceAll("&#x3e;", ">");
		value = value.replaceAll("&#62;", ">");

		value = value.replaceAll("&quot;", "\"");
		value = value.replaceAll("&#x22;", "\"");
		value = value.replaceAll("&#34;", "\"");
		
		value = value.replaceAll("&#x26;", "&");
		value = value.replaceAll("&#38;", "&");
		value = value.replaceAll("&amp;", "&");
		
		value = decodeApostrophes(value);
		
		return value;
	}

	public static String getXmlEncoded(String value)
	{
		value = value.replaceAll("&", "&amp;");
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("\"", "&quot;");
		value = getXmlEncodedApostrophes(value);
		
		return value;
	}

	public static String getXmlEncodedApostrophes(String value)
	{
		return value.replaceAll("'", "&apos;");
	}

	public static String convertXmlTextToHtmlWithHtmlTags(final String value)
	{
		String html = XmlUtilities2.convertXmlTextToHtmlWithoutHtmlTags(value);
		html = HtmlUtilities.wrapInHtmlTags(html);

		return html;
	}
	
	public static String convertXmlTextToHtmlWithoutHtmlTags(final String value)
	{
		ensureValidXmlWithHtmlTags(value);
		
		return decodeApostrophes(value);
	}

	private static String decodeApostrophes(String value)
	{
		value = value.replaceAll("&#39;", "'");
		value = value.replaceAll("&#x27;", "'");
		value = value.replaceAll("&apos;", "'");
		
		return value;
	}
	
	public static void ensureValidXmlWithHtmlTags(final String value)
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
		final String decodedValue = getXmlDecoded(value);
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

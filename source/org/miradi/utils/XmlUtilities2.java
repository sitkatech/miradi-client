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
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.miradi.main.EAM;

public class XmlUtilities2
{
	public static String getWithUnescapedNumericEntities(String value)
	{
		AggregateTranslator translator =  new AggregateTranslator(new NumericEntityUnescaper());
		
		return translator.translate(value);
	}
	
	public static String getXmlDecoded(String value)
	{
		if (isEmptyValue(value))
			return value;
		
		return StringEscapeUtils.unescapeXml(value);
	}

	public static String getXmlEncoded(String value)
	{
		if (isEmptyValue(value))
			return value;
		
		return StringEscapeUtils.escapeXml(value);
	}

	private static boolean isEmptyValue(String value)
	{
		return value == null || value.length() == 0;
	}

	public static String getXmlEncodedApostrophes(String value)
	{
		return value.replaceAll("'", "&apos;");
	}
	
	public static String getXmlEncodedDoubleQuotes(String value)
	{
		return value.replaceAll("\"", "&quot;");
	}
	
	public static String convertXmlTextToHtml(final String value)
	{
		try
		{
			String html = convertXmlTextToHtmlWithoutSurroundingHtmlTags(value);
			html = HtmlUtilities.wrapInHtmlTags(html);

			return html;
		}
		catch (Exception e)
		{
			EAM.logError("Invalid XML: " + value);
			EAM.logException(e);
			
			return Translation.getCellTextWhenException();
		}
	}
	
	public static String convertXmlTextToHtmlWithoutSurroundingHtmlTags(final String value) throws RuntimeException
	{
		try
		{
			logWarningIfNotValidHtml(value);
		}
		catch(RuntimeException e)
		{
			EAM.logError("Invalid XML: " + value);
			EAM.logException(e);
			
			throw e;
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
	public static void logWarningIfNotValidHtml(final String value)
	{
		if (value == null)
			return;
		
		String valueWithoutHtmlTags = HtmlUtilities.stripAllHtmlTags(value);
		//NOTE: The test method is not totally accurate because it
		//does not handle all characters, like non breaking space  &nbsp;
		//NOTE: this safety mechanism might be a speed concern
		if (!isValidXmlWithNoHtmlTags(valueWithoutHtmlTags))
			EAM.logWarning("Invalid xml value =" + valueWithoutHtmlTags);
	}
	
	public static boolean isValidXmlWithNoHtmlTags(final String value)
	{
		final String decodedValue = getXmlDecoded(value);
		final String encodedValue = getXmlEncoded(decodedValue);
		
		return encodedValue.equals(value);
	}
}

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
		value = value.replaceAll("&lt;", "<");
		value = value.replaceAll("&gt;", ">");
		value = value.replaceAll("&quot;", "\"");
		value = value.replaceAll("&apos;", "'");
		value = value.replaceAll("&amp;", "&");
		
		value = value.replaceAll("&#3Cx;", "<");
		value = value.replaceAll("&#3Ex;", ">");
		value = value.replaceAll("&#22x;", "\"");
		value = value.replaceAll("&#27x;", "'");
		value = value.replaceAll("&#26x;", "&");

		value = value.replaceAll("&#39;", "'");
		value = value.replaceAll("&#34;", "\"");
		value = value.replaceAll("&#38;", "&");
		value = value.replaceAll("&#60;", "<");
		value = value.replaceAll("&#62;", ">");
		
		return value;
	}

	public static String getXmlEncoded(String value)
	{
		value = value.replaceAll("&", "&amp;");
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("\"", "&quot;");
		value = value.replaceAll("'", "&apos;");
		
		return value;
	}
}

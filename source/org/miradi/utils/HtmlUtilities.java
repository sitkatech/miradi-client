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

import java.util.regex.Pattern;

import org.martus.util.xml.XmlUtilities;

public class HtmlUtilities
{
	public static String plainStringWithNewlinesToHtml(String plainString)
	{
		if(plainString == null)
			return "";
		
		String formatted =  XmlUtilities.getXmlEncoded(plainString);
		String formattedFactorName = replaceNonHtmlNewlines(formatted);
		return formattedFactorName;
	}

	public static String replaceNonHtmlNewlines(String formatted)
	{
		return formatted.replaceAll(NEW_LINE, BR_TAG);
	}
	
	public static String removeNonHtmlNewLines(String htmlText)
	{
		htmlText = htmlText.replaceAll(NEW_LINE, "");
		
		return htmlText;
	}
	
	public static String stripAllHtmlTags(String text)
	{
		final String ANY = "<.*?>";
		return replaceAll(ANY, text, "");
	}

	public static String replaceHtmlTags(String text, String tagToReplace, final String replacement)
	{
		final String START = "<" + tagToReplace + "\\s*>";
		final String START_WITH_ATRIBUTE = "<" + tagToReplace + "\\s+.*?>";
		final String END = "<\\/" + tagToReplace + "\\s*>";
		final String EMPTY = "<" + tagToReplace + "\\s*/\\s*>";
		final String regex = START + "|" + EMPTY + "|" + END + "|" + START_WITH_ATRIBUTE; 
		return replaceAll(regex, text, replacement);
	}
	
	private static String replaceAll(final String regex, String text, final String replacement)
	{
		final Pattern compiledRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		return compiledRegex.matcher(text).replaceAll(replacement);
	}
	
	public static String appendNewlineToEndDivTags(String text)
	{
		String replaced = text.replaceAll("</div>", "</div>" + HtmlUtilities.NEW_LINE);
		replaced = replaced.replaceAll("<div/>", "<div/>" + HtmlUtilities.NEW_LINE);
		
		return replaced;
	}
	
	public static String removeAllExcept(String text, String[] tagsToKeep)
	{
		String tagsSeperatedByOr = concatenateWithOr(tagsToKeep);
		
		String regex = "<\\/*?(?![^>]*?\\b(?:" + tagsSeperatedByOr + ")\\b)[^>]*?>";;
		final Pattern compiledRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		return compiledRegex.matcher(text).replaceAll("");
	}

	private static String concatenateWithOr(String[] tagsToKeep)
	{
		String tagsSeperatedByOr = "";
		for (int index = 0; index < tagsToKeep.length; ++index)
		{
			if (index != 0)
				tagsSeperatedByOr += "|";
			
			tagsSeperatedByOr += tagsToKeep[index];
		}
		
		return tagsSeperatedByOr;
	}

	public static final String BR_TAG = "<br/>";
	public static final String BR_TAG_UNCLOSED = "<br>";
	public static final String NEW_LINE = "\n";
}

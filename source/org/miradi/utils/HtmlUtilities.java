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
			return EMPTY_STRING;
		
		String formatted =  XmlUtilities.getXmlEncoded(plainString);
		
		return replaceNonHtmlNewlines(formatted);
	}
	
	public static String convertToHtmlText(String nonHtmlText)
	{
		nonHtmlText = XmlUtilities.getXmlEncoded(nonHtmlText);
		nonHtmlText = nonHtmlText.replaceAll("\n", HtmlUtilities.BR_TAG);
		nonHtmlText = nonHtmlText.replaceAll("\r", HtmlUtilities.BR_TAG);
		
		return nonHtmlText;
	}
	
	public static String convertToNonHtml(String htmlDataValue)
	{
		htmlDataValue = replaceHtmlBrsWithNewlines(htmlDataValue);
		htmlDataValue = XmlUtilities2.getXmlDecoded(htmlDataValue);
		
		return htmlDataValue;
	}

	public static String prepareForSaving(final String text, String[] htmlTagsToKeep)
	{
		String trimmedText = "";
		final String[] lines = text.split("\\r?\\n");
		for (int index = 0; index < lines.length; ++index)
		{
			trimmedText += lines[index].trim();
		}
		
		trimmedText = removeNonHtmlNewLines(trimmedText);
		trimmedText = appendNewlineToEndDivTags(trimmedText);
		trimmedText = removeAllExcept(trimmedText, htmlTagsToKeep);
		trimmedText = trimmedText.trim();
		trimmedText = replaceNonHtmlNewlines(trimmedText);
		
		return trimmedText;
	}
	
	public static String replaceHtmlBrsWithNewlines(String text)
	{
		return HtmlUtilities.replaceHtmlTags(text, "br", NEW_LINE);
	}

	public static String replaceNonHtmlNewlines(String formatted)
	{
		return formatted.replaceAll(NEW_LINE, BR_TAG);
	}
	
	public static String removeNonHtmlNewLines(String htmlText)
	{
		return htmlText.replaceAll(NEW_LINE, EMPTY_STRING);
	}

	public static String stripAllHtmlTags(String text)
	{
		final String ANY = "<.*?>";
		return replaceAll(ANY, text, EMPTY_STRING);
	}

	public static String replaceHtmlTags(String text, String tagToReplace, final String replacement)
	{
		final String START = "<" + tagToReplace + "\\s*>";
		final String START_WITH_ATRIBUTE = "<" + tagToReplace + "\\s+.*?>";
		final String END = createEndTagRegex(tagToReplace);
		final String EMPTY = createEmptyTagRegex(tagToReplace);
		final String regex = START + "|" + EMPTY + "|" + END + "|" + START_WITH_ATRIBUTE; 
		return replaceAll(regex, text, replacement);
	}

	private static String createEmptyTagRegex(String tagToReplace)
	{
		return "<" + tagToReplace + "\\s*/\\s*>";
	}

	private static String createEndTagRegex(String tag)
	{
		return "<\\/\\s*" + tag + "\\s*>";
	}
	
	private static String replaceAll(final String regex, String text, final String replacement)
	{
		final Pattern compiledRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		return compiledRegex.matcher(text).replaceAll(replacement);
	}
	
	public static String appendNewlineToEndDivTags(String text)
	{
		final String END = createEndTagRegex(DIV_TAG_NAME);
		final String EMPTY = createEmptyTagRegex(DIV_TAG_NAME);
		final String regex = EMPTY + "|" + END;
		
		return replaceAll(regex, text, DIV_CLOSING_TAG + HtmlUtilities.NEW_LINE);
	}
	
	public static String removeAllExcept(String text, String[] tagsToKeep)
	{
		String tagsSeperatedByOr = StringUtilities.joinWithOr(tagsToKeep);
		
		String regex = "<\\/*?(?![^>]*?\\b(?:" + tagsSeperatedByOr + ")\\b)[^>]*?>";;
		final Pattern compiledRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		return compiledRegex.matcher(text).replaceAll(EMPTY_STRING);
	}

	public static final String BR_TAG = "<br/>";
	public static final String NEW_LINE = "\n";
	public static final String EMPTY_STRING = "";
	private static final String DIV_TAG_NAME = "div";
	private static final String DIV_CLOSING_TAG = "</div>";
}

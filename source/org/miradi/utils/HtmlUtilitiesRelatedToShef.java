/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

public class HtmlUtilitiesRelatedToShef
{
	//TODO medium : this method should be split into two: sanitize and normalize
	//NOTE: This method does not attempt to normalize encoding of numeric/named entities like &#160; and &nbsp;
	public static String getNormalizedAndSanitizedHtmlText(String text, String[] allowedHtmlTags) throws Exception
	{
		if (text == null || text.length() == 0)
			return text;
		
		text = HtmlUtilities.stripHtmlComments(text);
		text = HtmlUtilities.replaceAllEmptyDivsWithBrs(text);
		text = HtmlUtilities.replaceEndParagraphTagsWithBrs(text);
		
		StringBuffer stringBuffer = new StringBuffer();
		final String[] lines = text.split(HtmlUtilities.getNewlineRegex());
		for (int index = 0; index < lines.length; ++index)
		{
			//NOTE: Shef editor never splits text between lines, so we can safely ignore the text\ntext case
			String line = lines[index];
			//FIXME - medium - Write a failing test for the line below, it might not be doing anything. 
			String leadingSpacesRemoved = line.replaceAll("^[ \\t]+", "");
			stringBuffer.append(leadingSpacesRemoved);
			stringBuffer.append(StringUtilities.EMPTY_SPACE);
		}
		
		String trimmedText = stringBuffer.toString();
		// NOTE: The Java HTML parser compresses all whitespace to a single space
		// (http://java.sun.com/products/jfc/tsc/articles/bookmarks/)
		trimmedText = HtmlUtilities.removeNonHtmlNewLines(trimmedText);
		trimmedText = HtmlUtilities.appendNewlineToEndDivTags(trimmedText);
		trimmedText = HtmlUtilities.removeAllExcept(trimmedText, allowedHtmlTags);
		trimmedText = HtmlUtilities.stripAttributesFromNonAnchorElements(trimmedText);
		trimmedText = trimmedText.replaceAll("\\t", " ");
		trimmedText = trimmedText.replaceAll(" +", " ");
		trimmedText = trimmedText.trim();		
		trimmedText = HtmlUtilities.replaceNonHtmlNewlines(trimmedText);
		//NOTE: Third party library  uses <br> instead of <br/>.  If we don't replace <br> then 
		//save method thinks there was a change and attempts to save.
		trimmedText = HtmlUtilities.replaceStartBrTagsWithEmptyBrTags(trimmedText);
		trimmedText = HtmlUtilities.getWithoutSpacesAfterXmlElements(trimmedText);
		// NOTE: Shef does not encode/decode apostrophes as we need for proper XML
		trimmedText = XmlUtilities2.getXmlEncodedApostrophes(trimmedText);
		HtmlUtilities.ensureNoCloseBrTags(trimmedText);
		trimmedText = HtmlUtilities.fixAnchorElements(trimmedText, allowedHtmlTags);
		
		return trimmedText;
	}
	
	
}

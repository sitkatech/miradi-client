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

import java.awt.Color;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.text.html.StyleSheet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class HtmlUtilities
{
	public static String wrapInHtmlTags(String value)
	{
		return wrapWithTag(value, "html");
	}

	public static String wrapWithTag(String value, String tagName)
	{
		return "<" + tagName + ">" + value + "</" + tagName + ">";
	}
	
	public static void addRuleFontSize(StyleSheet style, final int defaultFontSize, final int fontSize)
	{
		int size = fontSize;
		if (fontSize == 0)
			size = defaultFontSize;
		
		style.addRule(HtmlUtilities.makeSureRuleHasRightPrefix("body {font-size:"+size+"pt;}"));
	}
	
	public static void addRuleFontFamily(StyleSheet style, final String fontFamily)
	{
		style.addRule(HtmlUtilities.makeSureRuleHasRightPrefix("body {font-family:" + fontFamily + ";}"));
	}
	
	public static void addFontColor(StyleSheet style, Color color)
	{
		style.addRule(HtmlUtilities.makeSureRuleHasRightPrefix("body {color:" + AppPreferences.convertToHexString(color) + ";}"));
	}
	
	public static String makeSureRuleHasRightPrefix(String rule)
	{
		if (cssDotPrefixWorksCorrectly())
			return rule;

		return replaceDotWithPoundSign(rule);
	}
	
	public static boolean cssDotPrefixWorksCorrectly()
	{
		String javaVersion = EAM.getJavaVersion();
		if (javaVersion.startsWith("1.4"))
			return false;
		return true;
	}
	
	private static String replaceDotWithPoundSign(String rule)
	{
		if (rule.trim().startsWith("."))
			return rule.trim().replaceFirst(".", "#");

		return rule;
	}
	
	public static String convertStoredXslToNative(String storedXslTemplate)
	{
		String nativeXslTemplate = HtmlUtilities.replaceHtmlBrsWithNewlines(storedXslTemplate);
		nativeXslTemplate = XmlUtilities2.getXmlDecoded(nativeXslTemplate);
		
		return nativeXslTemplate;
	}
	
	public static String convertNativeXslToStored(String nativeXslTemplate)
	{
		String storedXslTemplate = XmlUtilities2.getXmlEncoded(nativeXslTemplate);
		storedXslTemplate = replaceNonHtmlNewlines(storedXslTemplate);
		
		return storedXslTemplate;
	}

	public static String convertPlainTextToHtmlText(String nonHtmlText)
	{
		nonHtmlText = XmlUtilities2.getXmlEncoded(nonHtmlText);
		nonHtmlText = replaceNonHtmlNewlines(nonHtmlText);
		
		return nonHtmlText;
	}
	
	public static String convertHtmlToPlainText(String htmlDataValue)
	{
		htmlDataValue = replaceHtmlBrsWithNewlines(htmlDataValue);
		htmlDataValue = XmlUtilities2.getXmlDecoded(htmlDataValue);
		htmlDataValue = HtmlUtilities.replaceHtmlBullets(htmlDataValue);
		htmlDataValue = stripAllHtmlTags(htmlDataValue);
		
		return htmlDataValue;
	}

	public static void ensureNoCloseBrTags(String text)
	{
		if (text.contains("</br>"))
			throw new RuntimeException("Text contains </br> tag(s)");		
	}

	public static String replaceStartBrTagsWithEmptyBrTags(String text)
	{
		return replaceHtmlTags(text, "br", BR_TAG);
	}

	public static String getNewlineRegex()
	{
		return "\\r?\\n";
	}
	
	public static String replaceHtmlBrsWithNewlines(String text)
	{
		return replaceHtmlTags(text, "br", StringUtilities.NEW_LINE);
	}

	public static String replaceNonHtmlNewlines(String formatted)
	{
		return formatted.replaceAll(getNewlineRegex(), BR_TAG);
	}
	
	public static String removeNonHtmlNewLines(String htmlText)
	{
		return htmlText.replaceAll(getNewlineRegex(), StringUtilities.EMPTY_STRING);
	}

	public static String stripAllHtmlTags(String text)
	{
		final String ANY = "<.*?>";
		return replaceAll(ANY, text, StringUtilities.EMPTY_STRING);
	}

	public static String replaceHtmlTags(String text, String tagToReplace, final String replacement)
	{
		final String START = createStartTagRegex(tagToReplace);
		final String START_WITH_ATRIBUTE = createStartTagWithAttributeRegex(tagToReplace);
		final String END = createEndTagRegex(tagToReplace);
		final String EMPTY = createEmptyTagRegex(tagToReplace);
		final String regex = START + "|" + EMPTY + "|" + END + "|" + START_WITH_ATRIBUTE; 
		
		return replaceAll(regex, text, replacement);
	}

	public static String replaceStartHtmlTags(String text, String tagToReplace, final String replacement)
	{
		final String START = createStartTagRegex(tagToReplace);
		final String START_WITH_ATRIBUTE = createStartTagWithAttributeRegex(tagToReplace);
		final String regex = START + "|" + START_WITH_ATRIBUTE;
		
		return replaceAll(regex, text, replacement);
	}
	
	private static String createStartTagWithAttributeRegex(String tagToReplace)
	{
		return "<\\s*" + tagToReplace + "\\s+.*?>";
	}

	private static String createStartTagRegex(String tagToReplace)
	{
		return "<\\s*" + tagToReplace + "\\s*>";
	}

	private static String createEmptyTagRegex(String tagToReplace)
	{
		return "<\\s*" + tagToReplace + "\\s*/\\s*>";
	}

	private static String createEndTagRegex(String tag)
	{
		return "<\\s*\\/\\s*" + tag + "\\s*>";
	}
	
	private static String replaceAll(final String regex, final String text, final String replacement)
	{
		final Pattern compiledRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		return compiledRegex.matcher(text).replaceAll(replacement);
	}
	
	public static String appendNewlineToEndDivTags(String text)
	{
		final String END_DIV_REGEX = createEndTagRegex(DIV_TAG_NAME);
		text = replaceAll(END_DIV_REGEX, text, DIV_CLOSING_TAG + StringUtilities.NEW_LINE);
		final String EMPTY_DIV_REGEX = createEmptyTagRegex(DIV_TAG_NAME);
		text = replaceAll(EMPTY_DIV_REGEX, text, DIV_EMPTY_TAG + StringUtilities.NEW_LINE);
		return text;
	}
	
	public static String removeAllExcept(String text, String[] tagsToKeep)
	{
		String tagsSeperatedByOr = StringUtilities.joinWithOr(tagsToKeep);
		
		String regex = "<\\/*?(?![^>]*?\\b(?:" + tagsSeperatedByOr + ")\\b)[^>]*?>";;
		final Pattern compiledRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		return compiledRegex.matcher(text).replaceAll(StringUtilities.EMPTY_STRING);
	}
	
	public static String getWithoutSpacesAfterXmlElements(final String text)
	{
		String result = replaceAll(" +(<.+>)", text, " $1");
		result = replaceAll("(<.+>) +", result, "$1 ");
		result = replaceAll("(<br/>) +", result, "$1");
		result = replaceAll(" +(<br/>)", result, "$1");
		
		return result;
	}

	static String replaceAllEmptyDivsWithBrs(String text)
	{
		final String START_DIV_REGEX = createStartTagRegex(DIV_TAG_NAME);
		final String END_DIV_REGEX = createEndTagRegex(DIV_TAG_NAME);
		text = text.replaceAll(START_DIV_REGEX + "\\s*" + END_DIV_REGEX, BR_TAG);
		return text;
	}
	
	public static String replaceEndParagraphTagsWithBrs(String html)
	{
		return replaceAll(createEndTagRegex("p"), html, BR_TAG);
	}
	
	public static int getLabelLineCount(String labelToUse)
	{
		String label = labelToUse + "AvoidSplitTrimmingTrailingNewlines";
		String[] lines = label.split(BR_TAG);
		return lines.length;
	}
	
	public static String getFirstLineWithTruncationIndicated(final String value)
	{
		String firstLine = value;
		int newlineAt = value.indexOf(BR_TAG);
		if (newlineAt >= 0)
			firstLine = value.substring(0, newlineAt) + "...";
		
		return firstLine;
	}

	public static String createHtmlBulletList(Vector<String> labels)
	{
		Collections.sort(labels, String.CASE_INSENSITIVE_ORDER);
	
		StringBuffer result = new StringBuffer();
		for(int index = 0; index < labels.size(); ++index)
		{
			if(result.length() == 0)
				result.append(UL_START_TAG);
			
			String label = labels.get(index);
			result.append(LI_START_TAG);
			result.append(label);
			result.append(LI_END_TAG);
		}
		
		if(result.length() > 0)
			result.append(UL_END_TAG);
		
		return result.toString();
	}
	
	public static String replaceHtmlBullets(String value)
	{
		value = value.replaceAll(LI_START_TAG, "- ");
		value = value.replaceAll(LI_END_TAG, BR_TAG);
		value = replaceHtmlTags(value, "ul", "");
		
		return value;
	}
	
	public static String removeStartToEndTagAndItsContent(String htmlText, String[] tagsToStripWithTheirContent)
	{
		String htmlTextWithRemovedTagsAndItsContent = htmlText;
		htmlTextWithRemovedTagsAndItsContent = removeNonHtmlNewLines(htmlTextWithRemovedTagsAndItsContent);
		for(String tagToStripWithItsContent : tagsToStripWithTheirContent)
		{
			htmlTextWithRemovedTagsAndItsContent = removeStartToEndTagAndItsContent(htmlTextWithRemovedTagsAndItsContent, tagToStripWithItsContent);
		}
		
		return htmlTextWithRemovedTagsAndItsContent;
	}
	
	private static String removeStartToEndTagAndItsContent(String htmlText, String tagToStripWithItsContent)
	{
		return replaceAll(createStartTagRegex(tagToStripWithItsContent) + ".*?" + createEndTagRegex(tagToStripWithItsContent), htmlText, "");
	}
	
	public static String stripAttributesFromNonAnchorElements(String htmlText)
	{
		// NOTE: This regexp says:
		// < - Starts with an angle bracket
		// (?!a ) - Can't match if the element name is a
		// (?!A ) - Can't match if the element name is A
		// (\w*?) - Matches any other element name and captures it as $1
		//  .*? - Matches any attributes
		// > - Ends with an angle bracket
		final String regexToMatchEntityWithAttributes = "<(?!a )(?!A )(\\w*?) .*?>";
		
		return replaceAll(regexToMatchEntityWithAttributes, htmlText, "<$1>");
	}

	public static String stripHtmlComments(String htmlText)
	{
		String regex = "<!--[\\s\\S]*?-->";

		return htmlText.replaceAll(regex, StringUtilities.EMPTY_STRING);
	}
	
	public static String fixAnchorElements(String xmlText, String[] allowedHtmlTags) throws Exception
	{
		xmlText = wrapWithTag(xmlText, "xml");

		final String fixAnchorElements = fixAnchorElements(xmlText);

		return HtmlUtilities.removeAllExcept(fixAnchorElements, allowedHtmlTags);
	}

	private static String fixAnchorElements(String htmlText) throws Exception
	{
		Document document = createDomDocument(htmlText);
		NodeList anchorElements = document.getElementsByTagName(ANCHOR_ELEMENT_NAME);
		for (int index = 0; index < anchorElements.getLength(); ++index)
		{
			Element anchorNode = (Element) anchorElements.item(index);
			fixAnchorAttributesInPlace(document, anchorNode);	
		}
		
		return toXmlString(document);
	}

	private static void fixAnchorAttributesInPlace(Document document, Element anchorNode)
	{
		removeIllegalAnchorAttributesInPlace(anchorNode);
		ensureHrefAttributeExists(document, anchorNode.getAttributes());
	}

	private static void removeIllegalAnchorAttributesInPlace(Element anchorNode)
	{
		Vector<String> attributeNamesToRemove = getIllegalAttributeNames(anchorNode);
		removeIllegalAttributes(anchorNode, attributeNamesToRemove);
	}

	private static Vector<String> getIllegalAttributeNames(Element anchorNode)
	{
		Vector<String> attributeNamesToRemove = new Vector<String>();
		NamedNodeMap attributes = anchorNode.getAttributes();
		for(int index = 0; index < attributes.getLength(); ++index)
		{
			Node attribute = attributes.item(index);
			final String attributeName = attribute.getNodeName();
			if (!isLegalAnchorAttribute(attributeName))
				attributeNamesToRemove.add(attributeName);
		}
		
		return attributeNamesToRemove;
	}
	
	private static void removeIllegalAttributes(Element anchorNode, Vector<String> attributeNamesToRemove)
	{
		for(String attributeNameToRemove : attributeNamesToRemove)
		{
			anchorNode.removeAttribute(attributeNameToRemove);
		}
	}

	private static void ensureHrefAttributeExists(Document document, NamedNodeMap attributes)
	{
		if (attributes.getNamedItem(HREF_ATTRIBUTE_NAME) != null)
			return;
		
		Node emptyHrefAttribute = document.createAttribute(HREF_ATTRIBUTE_NAME);	
		attributes.setNamedItem(emptyHrefAttribute);
	}
	
	private static boolean isLegalAnchorAttribute(String attributeName)
	{
		Vector<String> legalAnchorAttributeNames = new Vector<String>();
		legalAnchorAttributeNames.add(HREF_ATTRIBUTE_NAME);
		legalAnchorAttributeNames.add(NAME_ATTRIBUTE_NAME);
		legalAnchorAttributeNames.add(TARGET_ATTRIBUTE_NAME);
		legalAnchorAttributeNames.add(TITLE_ATTRIBUTE_NAME);

		return legalAnchorAttributeNames.contains(attributeName);
	}
	
	private static Document createDomDocument(String htmlText) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(htmlText);
		InputSource inputSource = new InputSource(stringInputputStream);

		return documentBuilder.parse(inputSource);
	}
	
	private static String toXmlString(Document document) throws Exception
	{
		return toXmlString(new DOMSource(document));
	}

	public static String toXmlString(DOMSource domSource) throws Exception
	{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		
		final UnicodeXmlWriter writer = UnicodeXmlWriter.create();
		StreamResult result = new StreamResult(writer);
		transformer.transform(domSource, result);
		
		String xmlAsString = writer.toString();
		xmlAsString = enodeAllUserQuotesAndApostrophes(xmlAsString);
		return xmlAsString;
	}
	
	public static String enodeAllUserQuotesAndApostrophes(String value)
	{
		if (!value.contains("\"") && !value.contains("'"))
			return value;
		
		StringBuffer buffer = new StringBuffer();
		boolean isInsideTag = false;
		char[] chars = value.toCharArray();
		for(char character : chars)
		{
			String replacementChar = String.valueOf(character);
			if (character == '<')
				isInsideTag = true;
			
			if (character == '>')
				isInsideTag = false;
			
			if (!isInsideTag && character =='\'')
				replacementChar = "&apos;";
			
			if (!isInsideTag && character =='\"')
				replacementChar = "&quot;";

			buffer.append(replacementChar);
		}
		
		return buffer.toString();
	}
	
	public static final String BR_TAG = "<br/>";
	public static final String UL_START_TAG = "<ul>";
	public static final String UL_END_TAG = "</ul>";
	public static final String LI_START_TAG = "<li>";
	public static final String LI_END_TAG = "</li>";
	private static final String DIV_TAG_NAME = "div";
	private static final String DIV_CLOSING_TAG = "</div>";
	private static final String DIV_EMPTY_TAG = "<div/>";
	public static final String ANCHOR_ELEMENT_NAME = "a";
	public static final String HREF_ATTRIBUTE_NAME = "href";
	public static final String NAME_ATTRIBUTE_NAME = "name";
	public static final String TITLE_ATTRIBUTE_NAME = "title";
	public static final String TARGET_ATTRIBUTE_NAME = "target";
}



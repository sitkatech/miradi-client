/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.utils;

import org.conservationmeasures.eam.main.EAMTestCase;

public class TestJTextile extends EAMTestCase
{
	public TestJTextile(String name)
	{
		super(name);
	}
	
	public void testFontStyles() throws Exception
	{
		verifyStyle("_", "em");
		verifyStyle("__", "i");
		verifyStyle("*", "strong");
		verifyStyle("**", "b");
		verifyStyle("??", "cite");
		verifyStyle("-", "del");
		verifyStyle("+", "ins");
		verifyStyle("^", "sup");
		verifyStyle("~", "sub");
		verifyStyle("@", "code");
	}

	public void testParagraphs() throws Exception
	{
		String simpleString = "Hello world";
		assertEquals("\t<p>" + simpleString + "</p>\n \n \n", JTextile.textile(simpleString));
		String list = "* one\n* two\n";
		assertEquals("\t<li>one</li>\n\t<li>two</li>\n \n \n", JTextile.textile(list));
		String numberedList = "# one\n# two\n";
		assertEquals("\t<li>one</li>\n\t<li>two</li>\n \n \n", JTextile.textile(numberedList));
		String blockQuote = "bq. " + simpleString;
		assertEquals("\t<blockquote>" + simpleString + "</blockquote>\n \n \n", JTextile.textile(blockQuote));
		String url = "http://miradi.org/index.html";
		String blockQuoteCite = "bq(" + url + "). " + simpleString;
		assertEquals("\t<blockquote cite=\"" + url + "\">" + simpleString + "</blockquote>\n \n \n", JTextile.textile(blockQuoteCite));
		String headerClass = "h2(class). " + simpleString;
		assertEquals("\t<h2 class=\"class\">" + simpleString + "</h2>\n \n \n", JTextile.textile(headerClass));
		String header = "h3. " + simpleString;
		assertEquals("\t<h3>" + simpleString + "</h3>\n \n \n", JTextile.textile(header));
		String paragraphClass = "p(class). " + simpleString;
		assertEquals("\t<p class=\"class\">" + simpleString + "</p>\n \n \n", JTextile.textile(paragraphClass));
		String paragraph = "p. " + simpleString;
		assertEquals("\t<p>" + simpleString + "</p>\n \n \n", JTextile.textile(paragraph));
		
	}
	
	public void testNoTextile() throws Exception
	{
		String noTextile = "a ==*not bold*== b";
		assertEquals("\t<p>" + noTextile.replaceAll("==", "") + "</p>\n \n \n", JTextile.textile(noTextile));
	}
	
	public void testHyperLinks() throws Exception
	{
		String title = "Great site!";
		String text = "Go see it.";
		String url = "http://miradi.org/index.html";
		String link = "\"" + text + "\":" + url + " blah";
		String expectedForLink = "\t<p><a href=\"" + url + "\" title=\"\">" + text + "</a> blah</p>\n \n \n";
		assertEquals(expectedForLink, JTextile.textile(link));
		String linkWithTitle = "\"" + text + "(" + title + ")\":" + url + " blah";
		String expectedForLinkWithTitle = "\t<p><a href=\"" + url + "\" title=\"" + title + "\">" + text + "</a> blah</p>\n \n \n";
		assertEquals(expectedForLinkWithTitle, JTextile.textile(linkWithTitle));
		
	}
	
	public void testImages() throws Exception
	{
		String altText = "Alternate Text";
		String linkUrl = "http://miradi.org/index.html";
		String imageUrl = "http://miradi.org/images/logo.jpg";
		String text = "whatever";
		String imageOnly = "!" + imageUrl + "! " + text;
		String expectedForImageOnly = "\t<p><img src=\"" + imageUrl + "\" alt=\"\" /> " + text + "</p>\n \n \n";
		assertEquals(expectedForImageOnly, JTextile.textile(imageOnly));
		
		String imageWithAlt = "!" + imageUrl + "(" + altText + ")! " + text;
		String expectedForImageWithAlt = "\t<p><img src=\"" + imageUrl + "\" alt=\"" + altText + "\" /> " + text + "</p>\n \n \n";
		assertEquals(expectedForImageWithAlt, JTextile.textile(imageWithAlt));

		String imageWithLink = "!" + imageUrl + "!:" + linkUrl + " " + text;
		String expectedForImageWithLink = "\t<p><a href=\"" + linkUrl + "\"><img src=\"" + imageUrl + "\" alt=\"\" /></a> " + text + "</p>\n \n \n";
		assertEquals(expectedForImageWithLink, JTextile.textile(imageWithLink));
	}
	
	public void testAcronym() throws Exception
	{
		String acronym = "BRB";
		String definition = "Be Right Back";
		assertEquals("\t<p><acronym title=\"(" + definition + ")\">" + acronym + "</acronym> xyz</p>\n \n \n", JTextile.textile(acronym + "(" + definition + ") xyz"));
	}
	
	void verifyStyle(String code, String resultingTag) throws Exception
	{
		String textile = "plain " + code + "special" + code + " plain";
		String html = "\t<p>plain <" + resultingTag + ">special</" + resultingTag + "> plain</p>\n \n \n";
		assertEquals(html, JTextile.textile(textile));
	}
}

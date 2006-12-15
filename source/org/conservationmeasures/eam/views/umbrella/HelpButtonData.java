/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

public class HelpButtonData
{
	
	public HelpButtonData(Class resourceClassToUse, String titleToUse, String htmlFileToUse)
	{
		title = titleToUse;
		htmlFile = htmlFileToUse;
		resourceClass = resourceClassToUse;
	}
	
	public HelpButtonData(String titleToUse, String htmlFileToUse)
	{
		this(null, titleToUse, htmlFileToUse);
	}
	
	public String toString()
	{
		return "Title:" + title + " File:" + htmlFile ;
	}
	
	public static final String MORE_INFO_HTML = "MoreInfo.html";
	public static final String MORE_INFO = "More Info";
	public static final String EXAMPLES_HTML = "Examples.html";
	public static final String EXAMPLES = "Examples";
	public static final String WORKSHOPE_HTML = "Workshop.html";
	public static final String WORKSHOPE = "Workshope";
	public static final String COMING_ATTACTIONS = "Coming Attactions";
	public static final String COMING_ATTRACTIONS_HTML = "ComingAttractions.html";

	
	public String title;
	public String htmlFile;
	public Class resourceClass;
}

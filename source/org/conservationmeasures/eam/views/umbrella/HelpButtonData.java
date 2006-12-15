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
	
	public String title;
	public String htmlFile;
	public Class resourceClass;
}

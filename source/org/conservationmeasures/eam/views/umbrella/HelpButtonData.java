/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

public class HelpButtonData
{
	public HelpButtonData(String titleToUse, String htmlFileToUse)
	{
		title = titleToUse;
		htmlFile = htmlFileToUse;
	}
	
	public String toString()
	{
		return "Title:" + title + " File:" + htmlFile ;
	}
	
	public String title;
	public String htmlFile;
}

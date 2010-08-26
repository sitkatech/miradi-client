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
package org.miradi.views.umbrella;

import org.miradi.main.EAM;
import org.miradi.utils.Translation;

public class HelpButtonData
{
	
	public HelpButtonData(String titleToUse, String htmlFileToUse)
	{
		title = titleToUse;
		htmlFile = htmlFileToUse;
	}

	public String getHelpContents() throws Exception
	{
		return Translation.getHtmlContent(htmlFile);
	}
	
	public String toString()
	{
		return "Title:" + title + " File:" + htmlFile ;
	}
	
	public static final String MORE_INFO_HTML = "MoreInfo.html";
	public static final String MORE_INFO = EAM.text("Action|More Info");
	public static final String EXAMPLES_HTML = "Examples.html";
	public static final String EXAMPLES = EAM.text("Action|Examples");
	public static final String WORKSHOP_HTML = "Workshop.html";
	public static final String WORKSHOP = EAM.text("Action|Workshop");
	
	public static final String COMING_ATTACTIONS = EAM.text("Action|Coming Attractions");
	public static final String COMING_ATTRACTIONS_HTML = "help/ComingAttractions.html";
	public static final String CREDITS = EAM.text("Action|Credits");
	public static final String CREDITS_HTML = "help/Credits.html";
	public static final String ABOUT_BENETECH = EAM.text("Action|About Benetech");
	public static final String ABOUT_BENETECH_HTML = "help/AboutBenetech.html";
	public static final String ABOUT_CMP = EAM.text("Action|About the CMP");
	public static final String ABOUT_CMP_HTML = "help/AboutCMP.html";
	public static final String AGILE_SOFTWARE = EAM.text("Action|Agile Software");
	public static final String AGILE_SOFTWARE_HTML = "help/AgileSoftware.html";
	public static final String CMP_STANDARDS = EAM.text("Action|CMP Open Standards");
	public static final String CMP_STANDARDS_HTML = "help/CMPStandards.html";
	public static final String ADAPTIVE_MANAGEMENT = EAM.text("Action|Adaptive Management");
	public static final String ADAPTIVE_MANAGEMENT_HTML = "help/AdaptiveManagement.html";
	public static final String SUPPORT = EAM.text("Action|Support");
	public static final String SUPPORT_HTML = "help/Support.html";
	
	public static final String IMPORT_AND_EXPORT_HTML = "demo/DemoExportAndImport.html";
	public static final String CONFIGURE_EXPORT = "Configure Export";
	public static final String DEMO = "Demo";
	public static final String DEMO_AND_DATABASES = "Demo and Databases";
	
	public String title;
	protected String htmlFile;
}

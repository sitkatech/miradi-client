/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

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
	public static final String WORKSHOP_HTML = "Workshop.html";
	public static final String WORKSHOP = "Workshop";
	public static final String COMING_ATTACTIONS = "Coming Attactions";
	public static final String COMING_ATTRACTIONS_HTML = "ComingAttractions.html";
	public static final String CREDITS = "Credits";
	public static final String CREDITS_HTML = "Credits.html";
	public static final String ABOUT_BENETECH = "About Benetech";
	public static final String ABOUT_BENETECH_HTML = "AboutBenetech.html";
	public static final String ABOUT_CMP = "About the CMP";
	public static final String ABOUT_CMP_HTML = "AboutCMP.html";
	public static final String AGILE_SOFTWARE = "Agile Software";
	public static final String AGILE_SOFTWARE_HTML = "AgileSoftware.html";
	public static final String CMP_STANDARDS = "CMP Standards";
	public static final String CMP_STANDARDS_HTML = "CMPStandards.html";
	public static final String ADAPTIVE_MANAGEMENT = "Adaptive Management";
	public static final String ADAPTIVE_MANAGEMENT_HTML = "AdaptiveManagement.html";
	public static final String IMPORT_AND_EXPORT_HTML = "export&import.html";
	public static final String CONFIGURE_EXPORT = "Configure Export";
	public static final String DEMO = "Demo";
	public static final String DEMO_AND_DATABASES = "Demo and Databases";
	public static final String SUPPORT = "Support";
	public static final String SUPPORT_HTML = "Support.html";
	
	public String title;
	public String htmlFile;
	public Class resourceClass;
}

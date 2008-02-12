/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.utils.HtmlViewPanel;

public class Definition
{
	public Definition(String termToUse, String defintionToUse)
	{
		definition = defintionToUse;
		term = termToUse;
	}
	
	public Definition(String termToUse, Class resourceClassToUse, String htmlFileNameToUse)
	{
		htmlFileName = htmlFileNameToUse;
		resourceClass = resourceClassToUse;
		term = termToUse;
	}
	
	public String getDefintion()
	{
		if (resourceClass==null)
			return definition;
		return HtmlViewPanel.loadResourceFile(resourceClass, htmlFileName);
	}
	
	
	public String term;
	private String definition;
	public Class resourceClass;
	public String htmlFileName;
}

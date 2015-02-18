/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

public class Definition
{
	public static Definition createDefinitionFromTextString(String termToUse, String defintionToUse)
	{
		return new Definition(termToUse, defintionToUse);
	}

	public static Definition createDefinitionFromHtmlFilename(String termToUse, String htmlFileNameToUse)
	{
		String definition = loadHtmlDefinition(htmlFileNameToUse);
		return createDefinitionFromTextString(termToUse, definition);
	}

	private Definition(String termToUse, String defintionToUse)
	{
		definition = defintionToUse;
		term = termToUse;
	}
	
	public String getDefintion()
	{
		return definition;
	}
	
	private static String loadHtmlDefinition(String htmlFileName)
	{
		try
		{
			return Translation.getHtmlContent(htmlFileName);
		}
		catch(Exception e)
		{
			EAM.logWarning("Missing file: " + htmlFileName);
			EAM.logStackTrace();
			return EAM.text("(Definition Not Available)");
		}
	}
	
	
	public String term;
	private String definition;
}

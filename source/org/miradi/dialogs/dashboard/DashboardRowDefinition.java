/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import java.util.Vector;

public class DashboardRowDefinition
{
	public DashboardRowDefinition(String rightColumnTemplateToUse)
	{
		this(rightColumnTemplateToUse, new Vector<String>());
	}

	public DashboardRowDefinition(String rightColumnTemplateToUse, String pseudoTagToUse)
	{
		this(rightColumnTemplateToUse, createVector(pseudoTagToUse));
	}
	
	public DashboardRowDefinition(String rightColumnTemplateToUse, String pseudoTag1ToUse, String pseudoTag2ToUse)
	{
		this(rightColumnTemplateToUse, createVector(pseudoTag1ToUse, pseudoTag2ToUse));
	}
	
	private DashboardRowDefinition(String rightColumnTemplateToUse, Vector<String> pseudoTagsToUse)
	{
		rightColumnTemplate = rightColumnTemplateToUse;
		pseudoTags = pseudoTagsToUse;
	}
	
	private static Vector<String> createVector(String string)
	{
		Vector<String> list = new Vector<String>();
		list.add(string);
		
		return list;
	}
	
	private static Vector<String> createVector(String string1, String string2)
	{
		Vector<String> list = new Vector<String>();
		list.add(string1);
		list.add(string2);
		
		return list;
	}
	
	public String getRightColumnTemplate()
	{
		return rightColumnTemplate;
	}
	
	public Vector<String> getPseudoTags()
	{
		return pseudoTags;
	}

	private String rightColumnTemplate;
	private Vector<String> pseudoTags;
}

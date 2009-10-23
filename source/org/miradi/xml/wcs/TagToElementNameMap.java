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

package org.miradi.xml.wcs;

import java.util.HashMap;

import org.miradi.objects.ProjectResource;

public class TagToElementNameMap implements WcsXmlConstants
{
	public TagToElementNameMap()
	{
		createTagToElementMap();
	}
	
	public String findElementName(String objectName, String tagToUse)
	{	
		if (fieldToElementMap.containsKey(objectName))
		{
			HashMap<String, String> objectMap = fieldToElementMap.get(objectName);
			if (objectMap.containsKey(tagToUse))
				return objectMap.get(tagToUse);
		}
		
		return tagToUse;
	}
	
	private void createTagToElementMap()
	{
		fieldToElementMap = new HashMap<String, HashMap<String,String>>();
		
		fieldToElementMap.put(PROJECT_RESOURCE, createProjectResourceMap());
	}

	private HashMap<String, String> createProjectResourceMap()
	{
		HashMap<String, String> projectResourceMap = new HashMap<String, String>();
		projectResourceMap.put(ProjectResource.TAG_LABEL, "Name");
		projectResourceMap.put(ProjectResource.TAG_GIVEN_NAME, "GiveName");
		
		return projectResourceMap;
	}
	
	private HashMap<String, HashMap<String, String>> fieldToElementMap;
}

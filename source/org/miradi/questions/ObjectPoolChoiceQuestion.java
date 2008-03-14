/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class ObjectPoolChoiceQuestion extends ObjectQuestion
{
	public ObjectPoolChoiceQuestion(Project project, int type)
	{
		super(getAllObjects(project, type));
	}
	
	private static BaseObject[] getAllObjects(Project project, int type)
	{
		ORefList refList = project.getPool(type).getRefList();
		BaseObject[] objectList = new BaseObject[refList.size()];
		for (int i = 0; i < refList.size(); ++i)
		{
			objectList[i] = project.findObject(refList.get(i));
		}
		
		return objectList;
	}
}

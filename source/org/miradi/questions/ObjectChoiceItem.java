/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class ObjectChoiceItem extends ChoiceItem
{
	public ObjectChoiceItem(Project projectToUse, ORef wrappedRefToUse)
	{
		super(wrappedRefToUse.toString(), null);
		project = projectToUse;
		wrappedRef = wrappedRefToUse;
	}
	
	public String getLabel()
	{
		BaseObject foundObject = project.findObject(wrappedRef);
		return foundObject.getLabel();
	}
	
	private Project project;
	private ORef wrappedRef;
}

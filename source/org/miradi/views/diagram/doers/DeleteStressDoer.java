/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.views.diagram.DeleteAnnotationDoer;

public class DeleteStressDoer extends DeleteAnnotationDoer
{
	public boolean isAvailable()
	{
		if (getObjects().length == 0)
			return false;
		
		if (getSelectedObjectType() != Stress.getObjectType())
			return false;
		
		return true;
	}
	
	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return getSingleSelected(Target.getObjectType());  
	}

	public String[] getDialogText()
	{
		return new String[] { "Are you sure you want to delete this Stress?",};
	}

	public String getAnnotationIdListTag()
	{
		return Target.TAG_STRESS_REFS;
	}
	
	public int getAnnotationType()
	{
		return Stress.getObjectType();
	}
}

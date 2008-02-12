/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;

public class DeleteSubTargetDoer extends DeleteAnnotationDoer
{	
	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return getSingleSelected(Target.getObjectType());  
	}

	public String getAnnotationIdListTag()
	{
		return Target.TAG_SUB_TARGET_REFS;
	}

	public int getAnnotationType()
	{
		return SubTarget.getObjectType();
	}

	public String[] getDialogText()
	{
		return new String[] { EAM.text("Are you sure you want to delete this Nested Target?"),};
	}
}

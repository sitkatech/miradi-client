/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.SubTarget;
import org.conservationmeasures.eam.objects.Target;

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

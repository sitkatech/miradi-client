/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;


public class CreateSubTargetDoer extends CreateAnnotationDoer
{
	public String getAnnotationListTag()
	{
		return Target.TAG_SUB_TARGET_REFS;
	}

	public int getAnnotationType()
	{
		return SubTarget.getObjectType();
	}
}

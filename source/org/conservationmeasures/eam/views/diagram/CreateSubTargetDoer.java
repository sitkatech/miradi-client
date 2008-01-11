/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.SubTarget;
import org.conservationmeasures.eam.objects.Target;


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

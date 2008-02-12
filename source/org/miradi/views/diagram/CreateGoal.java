/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;

public class CreateGoal  extends CreateAnnotationDoer
{
	public int getAnnotationType()
	{
		return ObjectType.GOAL;
	}
	public String getAnnotationListTag()
	{
		return Factor.TAG_GOAL_IDS;
	}
}
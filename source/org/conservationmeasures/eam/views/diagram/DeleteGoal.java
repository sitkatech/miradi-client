/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;

public class DeleteGoal extends DeleteAnnotationDoer
{
	public String[] getDialogText()
	{
		return new String[] { "Are you sure you want to delete this Goal?",};
	}

	public String getAnnotationIdListTag()
	{
		return Factor.TAG_GOAL_IDS;
	}
	
	public int getAnnotationType()
	{
		return Goal.getObjectType();
	}
}

/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Objective;

public class DeleteObjective extends DeleteAnnotationDoer
{
	public String[] getDialogText()
	{
		return new String[] { "Are you sure you want to delete this Objective?",};
	}

	public String getAnnotationIdListTag()
	{
		return Factor.TAG_OBJECTIVE_IDS;
	}
	
	public int getAnnotationType()
	{
		return Objective.getObjectType();
	}
}

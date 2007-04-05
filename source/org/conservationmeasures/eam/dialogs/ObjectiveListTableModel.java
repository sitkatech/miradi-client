/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class ObjectiveListTableModel extends ObjectListTableModel
{
	public ObjectiveListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef.getObjectType(), nodeRef.getObjectId(), Factor.TAG_OBJECTIVE_IDS, ObjectType.OBJECTIVE, Objective.TAG_LABEL);
	}
}

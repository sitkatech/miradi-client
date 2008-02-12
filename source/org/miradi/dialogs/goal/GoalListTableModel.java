/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.goal;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.project.Project;

public class GoalListTableModel extends ObjectListTableModel
{
	public GoalListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef.getObjectType(), nodeRef.getObjectId(), Factor.TAG_GOAL_IDS, ObjectType.GOAL, Goal.TAG_LABEL);
	}
}

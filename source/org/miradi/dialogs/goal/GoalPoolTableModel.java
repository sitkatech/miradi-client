/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.goal;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Goal;
import org.miradi.project.Project;


public class GoalPoolTableModel extends ObjectPoolTableModel
{
	public GoalPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.GOAL, COLUMN_TAGS);
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		Goal.TAG_SHORT_LABEL,
		Goal.TAG_LABEL,
		Goal.PSEUDO_TAG_FACTOR,
	};

}

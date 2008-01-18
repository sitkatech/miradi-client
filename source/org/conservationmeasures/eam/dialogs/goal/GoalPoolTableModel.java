/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.goal;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;


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

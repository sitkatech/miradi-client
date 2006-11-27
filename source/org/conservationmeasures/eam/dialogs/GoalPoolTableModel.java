/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

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

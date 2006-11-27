/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.dialogs.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;


public class GoalPoolTableModel extends ObjectPoolTableModel
{
	public GoalPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.GOAL, new String[] {Goal.TAG_LABEL});
	}
}

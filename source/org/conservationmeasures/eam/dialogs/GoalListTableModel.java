/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;

public class GoalListTableModel extends ObjectListTableModel
{
	public GoalListTableModel(Project projectToUse, FactorId nodeId)
	{
		super(projectToUse, ObjectType.FACTOR, nodeId, Factor.TAG_GOAL_IDS, ObjectType.GOAL, Goal.TAG_LABEL);
	}
}

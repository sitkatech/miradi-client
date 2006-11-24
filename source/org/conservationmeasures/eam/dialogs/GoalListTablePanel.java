/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionDeleteGoal;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class GoalListTablePanel extends ObjectListTablePanel
{
	public GoalListTablePanel(Project projectToUse, Actions actions, FactorId nodeId)
	{
		super(projectToUse, ObjectType.GOAL, 
				new GoalListTableModel(projectToUse, nodeId), 
				(MainWindowAction)actions.get(ActionCreateGoal.class), 
				(ObjectsAction)actions.get(ActionDeleteGoal.class));
	}

}

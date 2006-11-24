/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ObjectiveListTablePanel extends ObjectListTablePanel
{
	public ObjectiveListTablePanel(Project projectToUse, Actions actions, FactorId nodeId)
	{
		super(projectToUse, ObjectType.OBJECTIVE, 
				new ObjectiveListTableModel(projectToUse, nodeId), 
				(MainWindowAction)actions.get(ActionCreateObjective.class), 
				(ObjectsAction)actions.get(ActionDeleteObjective.class));
	}

}

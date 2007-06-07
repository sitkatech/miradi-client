/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCloneGoal;
import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionDeleteGoal;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class GoalListTablePanel extends ObjectListTablePanel
{
	public GoalListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, ObjectType.GOAL, 
				new GoalListTableModel(projectToUse, nodeRef), 
				(MainWindowAction)actions.get(ActionCreateGoal.class), 
				(ObjectsAction)actions.get(ActionDeleteGoal.class));
		//TODO: this class should be refactored to take a action list...EAMAction can have a isObjectAction and isMainWindowAction so it can be
		// handeled in a loop at the parent level
		addButton(new PanelButton(actions.get(ActionCloneGoal.class)));
	}

}

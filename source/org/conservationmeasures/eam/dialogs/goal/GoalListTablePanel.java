/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.goal;

import org.conservationmeasures.eam.actions.ActionCloneGoal;
import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionDeleteGoal;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class GoalListTablePanel extends ObjectListTablePanel
{
	public GoalListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, new GoalListTableModel(projectToUse, nodeRef), 
				actions, 
				buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateGoal.class,
		ActionDeleteGoal.class,
		ActionCloneGoal.class
	};
}

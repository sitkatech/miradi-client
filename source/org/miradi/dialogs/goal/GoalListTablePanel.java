/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.goal;

import org.miradi.actions.ActionCloneGoal;
import org.miradi.actions.ActionCreateGoal;
import org.miradi.actions.ActionDeleteGoal;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

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

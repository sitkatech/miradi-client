/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.GoalId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class GoalPoolManagementPanel extends ObjectPoolManagementPanel
{
	public GoalPoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new GoalPoolTablePanel(projectToUse),
				new GoalPropertiesPanel(projectToUse, actions, new GoalId(BaseId.INVALID.asInt())));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Goals");
	}
	
	public Icon getIcon()
	{
		return new GoalIcon();
	}
}

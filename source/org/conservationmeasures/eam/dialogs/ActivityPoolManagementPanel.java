/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.ActivityIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class ActivityPoolManagementPanel extends ObjectPoolManagementPanel
{
	public ActivityPoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new ActivityPoolTablePanel(projectToUse),
				new ActivityPropertiesPanel(actions, projectToUse, BaseId.INVALID));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Activities");
	}
	
	public Icon getIcon()
	{
		return new ActivityIcon();
	}
}

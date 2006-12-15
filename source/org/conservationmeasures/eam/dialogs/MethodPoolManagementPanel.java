/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.MethodIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class MethodPoolManagementPanel extends ObjectPoolManagementPanel
{
	public MethodPoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new MethodPoolTablePanel(projectToUse),
				new TaskPropertiesPanel(projectToUse, actions, BaseId.INVALID));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Method");
	}
	
	public Icon getIcon()
	{
		return new MethodIcon();
	}
}
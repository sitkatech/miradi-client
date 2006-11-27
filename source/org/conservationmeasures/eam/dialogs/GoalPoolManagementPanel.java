/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.GoalId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class GoalPoolManagementPanel extends ModelessDialogPanel
{
	public GoalPoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new BorderLayout());
		GoalId invalidId = new GoalId(BaseId.INVALID.asInt());
		
		poolComponent = new GoalPoolTablePanel(projectToUse);
		add(poolComponent, BorderLayout.CENTER);
		
		propertiesPanel = new GoalPropertiesPanel(projectToUse, actions, invalidId);
		poolComponent.setPropertiesPanel(propertiesPanel);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void dispose()
	{
		poolComponent.dispose();
		poolComponent = null;
		
		propertiesPanel.dispose();
		propertiesPanel = null;
		
		super.dispose();
	}


	public EAMObject getObject()
	{
		return poolComponent.getSelectedObject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Tab|Goals");
	}
	
	public Icon getIcon()
	{
		return new GoalIcon();
	}
	
	
	GoalPoolTablePanel poolComponent;
	GoalPropertiesPanel propertiesPanel;
}

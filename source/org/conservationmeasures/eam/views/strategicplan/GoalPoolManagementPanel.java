/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.GoalPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
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
		
		PoolComponent = new GoalPoolTablePanel(projectToUse);
		add(PoolComponent, BorderLayout.CENTER);
		
		propertiesPanel = new GoalPropertiesPanel(projectToUse, actions, invalidId);
		PoolComponent.setPropertiesPanel(propertiesPanel);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void dispose()
	{
		PoolComponent.dispose();
		PoolComponent = null;
		
		propertiesPanel.dispose();
		propertiesPanel = null;
		
		super.dispose();
	}


	public EAMObject getObject()
	{
		return PoolComponent.getSelectedObject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Tab|Goals");
	}
	
	GoalPoolTablePanel PoolComponent;
	GoalPropertiesPanel propertiesPanel;
}

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.GoalId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiScrollPane;

public class GoalListManagementPanel extends ModelessDialogPanel
{
	public GoalListManagementPanel(Project projectToUse, FactorId nodeId, Actions actions) throws Exception
	{
		super(new BorderLayout());
		GoalId invalidId = new GoalId(BaseId.INVALID.asInt());
		
		listComponent = new GoalListTablePanel(projectToUse, actions, nodeId);
		add(listComponent, BorderLayout.CENTER);
		
		propertiesPanel = new GoalPropertiesPanel(projectToUse, actions, invalidId);
		listComponent.setPropertiesPanel(propertiesPanel);
		add(new UiScrollPane(propertiesPanel), BorderLayout.AFTER_LAST_LINE);
	}
	
	public void dispose()
	{
		listComponent.dispose();
		listComponent = null;
		
		propertiesPanel.dispose();
		propertiesPanel = null;
		
		super.dispose();
	}


	public EAMObject getObject()
	{
		return listComponent.getSelectedObject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Tab|Goals");
	}
	
	GoalListTablePanel listComponent;
	GoalPropertiesPanel propertiesPanel;
}

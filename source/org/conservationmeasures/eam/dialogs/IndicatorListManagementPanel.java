/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class IndicatorListManagementPanel extends ModelessDialogPanel
{
	public IndicatorListManagementPanel(Project projectToUse, ModelNodeId nodeId, Actions actions) throws Exception
	{
		super(new BorderLayout());
		IndicatorId invalidId = new IndicatorId(BaseId.INVALID.asInt());
		
		listComponent = new IndicatorListTablePanel(projectToUse, actions, nodeId);
		add(listComponent, BorderLayout.CENTER);
		
		propertiesPanel = new IndicatorPropertiesPanel(projectToUse, actions, invalidId);
		listComponent.setPropertiesPanel(propertiesPanel);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);
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
		return EAM.text("Tab|Indicators");
	}
	
	IndicatorListTablePanel listComponent;
	IndicatorPropertiesPanel propertiesPanel;
}


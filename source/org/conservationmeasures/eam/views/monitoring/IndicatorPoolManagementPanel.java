/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.IndicatorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class IndicatorPoolManagementPanel extends ModelessDialogPanel
{
	public IndicatorPoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new BorderLayout());
		IndicatorId invalidId = new IndicatorId(BaseId.INVALID.asInt());
		
		PoolComponent = new IndicatorPoolTablePanel(projectToUse);
		add(PoolComponent, BorderLayout.CENTER);
		
		propertiesPanel = new IndicatorPropertiesPanel(projectToUse, actions, invalidId);
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
		return EAM.text("Tab|Objectives");
	}
	
	IndicatorPoolTablePanel PoolComponent;
	IndicatorPropertiesPanel propertiesPanel;
}

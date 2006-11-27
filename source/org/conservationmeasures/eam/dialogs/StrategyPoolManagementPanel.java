/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class StrategyPoolManagementPanel extends ModelessDialogPanel
{
	public StrategyPoolManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(new BorderLayout());
		Project project = mainWindowToUse.getProject();
		poolComponent = new StrategyPoolTablePanel(project);
		add(poolComponent, BorderLayout.CENTER);
		
		BaseId invalidId = new BaseId(BaseId.INVALID.asInt());
		propertiesPanel = new StrategyPropertiesPanel(project, ObjectType.MODEL_NODE, invalidId);
		poolComponent.setPropertiesPanel(propertiesPanel);
		add(propertiesPanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void dispose()
	{
		super.dispose();
		poolComponent.dispose();
		poolComponent = null;
	}
	
	public EAMObject getObject()
	{
		return null;
	}

	public String getPanelDescription()
	{
		return EAM.text("Tab|Strategies");
	}
	
	public Icon getIcon()
	{
		return new StrategyIcon();
	}

	StrategyPoolTablePanel poolComponent;
	StrategyPropertiesPanel propertiesPanel;
}

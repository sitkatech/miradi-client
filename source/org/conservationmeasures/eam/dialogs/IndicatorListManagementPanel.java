/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class IndicatorListManagementPanel extends ObjectListManagementPanel
{
	public IndicatorListManagementPanel(Project projectToUse, FactorId nodeId, Actions actions) throws Exception
	{
		super(new IndicatorListTablePanel(projectToUse, actions, nodeId),
				new IndicatorPropertiesPanel(projectToUse, actions));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Indicators");
	}
	
	public Icon getIcon()
	{
		return new IndicatorIcon();
	}
}


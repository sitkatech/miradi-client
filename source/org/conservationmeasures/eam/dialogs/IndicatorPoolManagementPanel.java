/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class IndicatorPoolManagementPanel extends ObjectPoolManagementPanel
{
	public IndicatorPoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new IndicatorPoolTablePanel(projectToUse),
				new IndicatorPropertiesPanel(projectToUse, actions, new IndicatorId(BaseId.INVALID.asInt())));
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

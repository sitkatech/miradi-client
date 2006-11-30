/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class StrategyPoolManagementPanel extends ObjectPoolManagementPanel
{
	public StrategyPoolManagementPanel(Project projectToUse) throws Exception
	{
		super(new StrategyPoolTablePanel(projectToUse),
				new StrategyPropertiesPanel(projectToUse, ObjectType.FACTOR, BaseId.INVALID));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Strategies");
	}
	
	public Icon getIcon()
	{
		return new StrategyIcon();
	}
}

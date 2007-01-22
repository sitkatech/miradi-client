/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

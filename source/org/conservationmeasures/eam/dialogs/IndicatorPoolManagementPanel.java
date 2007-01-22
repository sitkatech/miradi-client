/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

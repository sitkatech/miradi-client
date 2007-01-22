/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.GoalId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class GoalPoolManagementPanel extends ObjectPoolManagementPanel
{
	public GoalPoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new GoalPoolTablePanel(projectToUse),
				new GoalPropertiesPanel(projectToUse, actions, new GoalId(BaseId.INVALID.asInt())));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Goals");
	}
	
	public Icon getIcon()
	{
		return new GoalIcon();
	}
}

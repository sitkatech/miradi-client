/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.ActivityIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class ActivityPoolManagementPanel extends ObjectPoolManagementPanel
{
	public ActivityPoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new ActivityPoolTablePanel(projectToUse),
				new TaskPropertiesPanel(projectToUse, actions, BaseId.INVALID));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Activities");
	}
	
	public Icon getIcon()
	{
		return new ActivityIcon();
	}
}

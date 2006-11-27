package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class ActivityListManagementPanel extends ObjectListManagementPanel
{
	public ActivityListManagementPanel(Project projectToUse, FactorId nodeId, Actions actions) throws Exception
	{
		super(projectToUse, nodeId, actions, 
				new ActivityListTablePanel(projectToUse, actions, nodeId),
				new ActivityPropertiesPanel(projectToUse, actions));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Activities");
	}
}

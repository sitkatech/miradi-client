/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaver;

public class GoalListManagementPanel extends ObjectListManagementPanel
{
	public GoalListManagementPanel(Project projectToUse, SplitterPositionSaver splitPositionSaverToUse, FactorId nodeId, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, PANEL_DESCRIPTION, new GoalListTablePanel(projectToUse, actions, nodeId),
				new GoalPropertiesPanel(projectToUse, actions));
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new GoalIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Goals"); 
}

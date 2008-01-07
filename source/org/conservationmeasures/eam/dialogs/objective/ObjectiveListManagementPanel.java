/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.objective;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.dialogs.base.ObjectListManagementPanel;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class ObjectiveListManagementPanel extends ObjectListManagementPanel
{
	public ObjectiveListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, new ObjectiveListTablePanel(projectToUse, actions, nodeRef),
				new ObjectivePropertiesPanel(projectToUse, actions));
	}
	
	public String getSplitterDescription()
	{
		return getPanelDescription() + SPLITTER_TAG;
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new ObjectiveIcon();
	}
	
	
	public Class getJumpActionClass()
	{
		return ActionJumpStrategicPlanDevelopObjectivesStep.class;
	}
	
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Objectives"); 
}

/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.objective;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class ObjectiveListManagementPanel extends ObjectListManagementPanel
{
	public ObjectiveListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, Actions actions, ObjectiveListTablePanel objectListPanel) throws Exception
	{
		super(splitPositionSaverToUse, objectListPanel, new ObjectivePropertiesPanel(projectToUse, actions, objectListPanel.getPicker()));
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

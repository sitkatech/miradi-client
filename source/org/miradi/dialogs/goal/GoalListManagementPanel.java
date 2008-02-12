/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.goal;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.GoalIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class GoalListManagementPanel extends ObjectListManagementPanel
{
	public GoalListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, new GoalListTablePanel(projectToUse, actions, nodeRef),
				new GoalPropertiesPanel(projectToUse));
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
		return new GoalIcon();
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpStrategicPlanDevelopGoalStep.class;
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Goals"); 
}
